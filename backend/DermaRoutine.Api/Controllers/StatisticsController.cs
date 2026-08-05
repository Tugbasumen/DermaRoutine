using DermaRoutine.Api.Data;
using DermaRoutine.Api.DTOs;
using DermaRoutine.Api.Entities;
using DermaRoutine.Api.Enums;
using DermaRoutine.Api.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class StatisticsController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;
        private readonly IRoutineStreakService _streakService;

        public StatisticsController(AppDbContext context, IUserContext userContext, IRoutineStreakService streakService)
        {
            _context = context;
            _userContext = userContext;
            _streakService = streakService;
        }

        [HttpGet("summary")]
        public async Task<ActionResult<ProgressSummaryDto>> GetSummary()
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var now = DateTime.UtcNow;
            var sevenDaysAgo = now.Date.AddDays(-6);
            var thirtyDaysAgo = now.Date.AddDays(-29);

            // 1. Calculate Routine Completion Rates (Last 7 Days)
            var morningRoutine = await _context.Routines
                .FirstOrDefaultAsync(r => r.UserId == userId && r.RoutineType == RoutineType.Morning);

            var eveningRoutine = await _context.Routines
                .FirstOrDefaultAsync(r => r.UserId == userId && r.RoutineType == RoutineType.Evening);

            double morningRate = 0;
            if (morningRoutine != null)
            {
                var completions = await _context.RoutineCompletions
                    .CountAsync(c => c.RoutineId == morningRoutine.Id && c.CompletedAt.Date >= sevenDaysAgo);
                morningRate = (completions / 7.0) * 100;
            }

            double eveningRate = 0;
            if (eveningRoutine != null)
            {
                var completions = await _context.RoutineCompletions
                    .CountAsync(c => c.RoutineId == eveningRoutine.Id && c.CompletedAt.Date >= sevenDaysAgo);
                eveningRate = (completions / 7.0) * 100;
            }

            // 2. Get Streak
            var streak = await _streakService.GetCurrentStreakAsync(userId.Value);

            // 3. Aggregate Moods (Last 30 Days)
            var moodStats = await _context.JournalEntries
                .Where(j => j.UserId == userId && j.CreatedAt.Date >= thirtyDaysAgo && !string.IsNullOrEmpty(j.Mood))
                .GroupBy(j => j.Mood)
                .Select(g => new MoodStatDto
                {
                    Mood = g.Key!,
                    Count = g.Count()
                })
                .OrderByDescending(x => x.Count)
                .ToListAsync();

            return Ok(new ProgressSummaryDto
            {
                MorningCompletionRate = Math.Round(morningRate, 1),
                EveningCompletionRate = Math.Round(eveningRate, 1),
                CurrentStreak = streak.CurrentStreak,
                MoodCounts = moodStats
            });
        }
    }
}
