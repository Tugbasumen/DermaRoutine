using DermaRoutine.Api.Data;
using DermaRoutine.Api.DTOs;
using DermaRoutine.Api.Entities;
using DermaRoutine.Api.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class RoutineController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;
        private readonly IRoutineStreakService _streakService;

        public RoutineController(AppDbContext context, IUserContext userContext, IRoutineStreakService streakService)
        {
            _context = context;
            _userContext = userContext;
            _streakService = streakService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<RoutineDto>>> GetRoutines()
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var today = DateTime.UtcNow.Date;
            var routines = await _context.Routines
                .Where(r => r.UserId == userId)
                .Select(r => new RoutineDto
                {
                    Id = r.Id,
                    Name = r.Name,
                    RoutineType = r.RoutineType,
                    IsActive = r.IsActive,
                    ItemCount = r.RoutineItems.Count,
                    CompletedItemCount = _context.RoutineItemCompletions
                        .Count(ric => ric.UserId == userId && ric.CompletedAt.Date == today && r.RoutineItems.Any(ri => ri.Id == ric.RoutineItemId)),
                    IsCompletedToday = r.Completions.Any(c => c.CompletedAt.Date == today)
                })
                .ToListAsync();

            return Ok(routines);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<RoutineDetailDto>> GetRoutine(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var today = DateTime.UtcNow.Date;
            var routine = await _context.Routines
                .Include(r => r.RoutineItems)
                    .ThenInclude(ri => ri.Product)
                .FirstOrDefaultAsync(r => r.Id == id && r.UserId == userId);

            if (routine == null) return NotFound();

            var dto = new RoutineDetailDto
            {
                Id = routine.Id,
                Name = routine.Name,
                RoutineType = routine.RoutineType,
                IsActive = routine.IsActive,
                ItemCount = routine.RoutineItems.Count,
                IsCompletedToday = await _context.RoutineCompletions.AnyAsync(c => c.RoutineId == id && c.CompletedAt.Date == today),
                Items = routine.RoutineItems.OrderBy(ri => ri.StepOrder).Select(ri => new RoutineItemDto
                {
                    Id = ri.Id,
                    ProductId = ri.ProductId,
                    ProductName = ri.Product.Name,
                    ProductBrand = ri.Product.Brand,
                    ProductImageUrl = ri.Product.ImageUrl,
                    StepOrder = ri.StepOrder,
                    Notes = ri.Notes,
                    IsCompletedToday = _context.RoutineItemCompletions
                        .Any(ric => ric.RoutineItemId == ri.Id && ric.UserId == userId && ric.CompletedAt.Date == today)
                }).ToList()
            };

            return Ok(dto);
        }

        [HttpPost]
        public async Task<ActionResult<RoutineDto>> CreateRoutine(CreateRoutineRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var routine = new Routine
            {
                UserId = userId.Value,
                Name = request.Name,
                RoutineType = request.RoutineType,
                IsActive = true,
                CreatedAt = DateTime.UtcNow,
                UpdatedAt = DateTime.UtcNow
            };

            _context.Routines.Add(routine);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetRoutine), new { id = routine.Id }, new RoutineDto
            {
                Id = routine.Id,
                Name = routine.Name,
                RoutineType = routine.RoutineType,
                IsActive = routine.IsActive
            });
        }

        [HttpPost("{id}/complete")]
        public async Task<IActionResult> CompleteRoutine(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var routine = await _context.Routines.AnyAsync(r => r.Id == id && r.UserId == userId);
            if (!routine) return NotFound();

            var today = DateTime.UtcNow.Date;
            var alreadyCompleted = await _context.RoutineCompletions
                .AnyAsync(c => c.RoutineId == id && c.CompletedAt.Date == today);

            if (alreadyCompleted) return BadRequest("Routine already completed today.");

            var completion = new RoutineCompletion
            {
                UserId = userId.Value,
                RoutineId = id,
                CompletedAt = DateTime.UtcNow
            };

            _context.RoutineCompletions.Add(completion);
            await _context.SaveChangesAsync();

            return Ok();
        }

        [HttpGet("streak")]
        public async Task<ActionResult<StreakDto>> GetStreak()
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var streak = await _streakService.GetCurrentStreakAsync(userId.Value);
            return Ok(streak);
        }

        [HttpPost("{id}/items")]
        public async Task<IActionResult> AddItemToRoutine(int id, AddRoutineItemRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var routine = await _context.Routines.FirstOrDefaultAsync(r => r.Id == id && r.UserId == userId);
            if (routine == null) return NotFound("Routine not found.");

            // Verify product ownership
            var product = await _context.Products.FirstOrDefaultAsync(p => p.Id == request.ProductId && p.UserId == userId);
            if (product == null) return BadRequest("Product not found or access denied.");

            var item = new RoutineItem
            {
                RoutineId = id,
                ProductId = request.ProductId,
                StepOrder = request.StepOrder,
                Notes = request.Notes
            };

            _context.RoutineItems.Add(item);
            await _context.SaveChangesAsync();

            return Ok();
        }

        [HttpDelete("{id}/items/{itemId}")]
        public async Task<IActionResult> RemoveItemFromRoutine(int id, int itemId)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var item = await _context.RoutineItems
                .Include(ri => ri.Routine)
                .FirstOrDefaultAsync(ri => ri.Id == itemId && ri.RoutineId == id && ri.Routine.UserId == userId);

            if (item == null) return NotFound();

            _context.RoutineItems.Remove(item);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpPost("items/{itemId}/toggle")]
        public async Task<IActionResult> ToggleItemCompletion(int itemId)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            // Verify item exists and belongs to user's routine
            var item = await _context.RoutineItems
                .Include(ri => ri.Routine)
                .FirstOrDefaultAsync(ri => ri.Id == itemId && ri.Routine.UserId == userId);

            if (item == null) return NotFound();

            var today = DateTime.UtcNow.Date;
            var completion = await _context.RoutineItemCompletions
                .FirstOrDefaultAsync(ric => ric.RoutineItemId == itemId && ric.UserId == userId && ric.CompletedAt.Date == today);

            if (completion != null)
            {
                _context.RoutineItemCompletions.Remove(completion);
            }
            else
            {
                _context.RoutineItemCompletions.Add(new RoutineItemCompletion
                {
                    RoutineItemId = itemId,
                    UserId = userId.Value,
                    CompletedAt = DateTime.UtcNow
                });
            }

            await _context.SaveChangesAsync();
            return Ok();
        }
    }
}
