using DermaRoutine.Api.Data;
using DermaRoutine.Api.DTOs;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Services
{
    public class RoutineStreakService : IRoutineStreakService
    {
        private readonly AppDbContext _context;

        public RoutineStreakService(AppDbContext context)
        {
            _context = context;
        }

        public async Task<StreakDto> GetCurrentStreakAsync(int userId)
        {
            var activeRoutines = await _context.Routines
                .Where(r => r.UserId == userId && r.IsActive)
                .Select(r => r.Id)
                .ToListAsync();

            if (!activeRoutines.Any())
            {
                return new StreakDto { CurrentStreak = 0, TodayCompleted = false };
            }

            var completions = await _context.RoutineCompletions
                .Where(rc => rc.UserId == userId)
                .OrderByDescending(rc => rc.CompletedAt)
                .ToListAsync();

            int streak = 0;
            var today = DateTime.UtcNow.Date;
            bool todayCompleted = IsDayCompleted(today, activeRoutines, completions);

            if (todayCompleted)
            {
                streak = 1;
                var checkDate = today.AddDays(-1);
                while (IsDayCompleted(checkDate, activeRoutines, completions))
                {
                    streak++;
                    checkDate = checkDate.AddDays(-1);
                }
            }
            else
            {
                // Streak might still be alive from yesterday
                var checkDate = today.AddDays(-1);
                while (IsDayCompleted(checkDate, activeRoutines, completions))
                {
                    streak++;
                    checkDate = checkDate.AddDays(-1);
                }
            }

            return new StreakDto
            {
                CurrentStreak = streak,
                TodayCompleted = todayCompleted
            };
        }

        private bool IsDayCompleted(DateTime date, List<int> activeRoutineIds, List<Entities.RoutineCompletion> completions)
        {
            var dayCompletions = completions
                .Where(c => c.CompletedAt.Date == date.Date)
                .Select(c => c.RoutineId)
                .Distinct()
                .ToList();

            // A day is completed if all active routines are completed
            return activeRoutineIds.All(id => dayCompletions.Contains(id));
        }
    }
}
