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
    public class SkinPhotoController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;

        public SkinPhotoController(AppDbContext context, IUserContext userContext)
        {
            _context = context;
            _userContext = userContext;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<SkinPhotoDto>>> GetPhotos()
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var photos = await _context.SkinPhotos
                .Where(p => p.UserId == userId)
                .OrderByDescending(p => p.TakenAt)
                .Select(p => new SkinPhotoDto
                {
                    Id = p.Id,
                    ImageUrl = p.ImageUrl,
                    TakenAt = p.TakenAt,
                    Rating = p.Rating,
                    Notes = p.Notes
                })
                .ToListAsync();

            return Ok(photos);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<SkinPhotoDto>> GetPhoto(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var photo = await _context.SkinPhotos
                .FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);

            if (photo == null) return NotFound();

            return Ok(new SkinPhotoDto
            {
                Id = photo.Id,
                ImageUrl = photo.ImageUrl,
                TakenAt = photo.TakenAt,
                Rating = photo.Rating,
                Notes = photo.Notes
            });
        }

        [HttpPost]
        public async Task<ActionResult<SkinPhotoDto>> CreatePhoto(CreateSkinPhotoRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var photo = new SkinPhoto
            {
                UserId = userId.Value,
                ImageUrl = request.ImageUrl,
                TakenAt = request.TakenAt,
                Rating = request.Rating,
                Notes = request.Notes,
                CreatedAt = DateTime.UtcNow
            };

            _context.SkinPhotos.Add(photo);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetPhoto), new { id = photo.Id }, new SkinPhotoDto
            {
                Id = photo.Id,
                ImageUrl = photo.ImageUrl,
                TakenAt = photo.TakenAt,
                Rating = photo.Rating,
                Notes = photo.Notes
            });
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdatePhoto(int id, UpdateSkinPhotoRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var photo = await _context.SkinPhotos
                .FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);

            if (photo == null) return NotFound();

            photo.TakenAt = request.TakenAt;
            photo.Rating = request.Rating;
            photo.Notes = request.Notes;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePhoto(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var photo = await _context.SkinPhotos
                .FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);

            if (photo == null) return NotFound();

            _context.SkinPhotos.Remove(photo);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}
