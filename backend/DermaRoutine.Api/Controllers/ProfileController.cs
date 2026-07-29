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
    public class ProfileController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;

        public ProfileController(AppDbContext context, IUserContext userContext)
        {
            _context = context;
            _userContext = userContext;
        }

        [HttpGet]
        public async Task<ActionResult<SkinProfileDto>> GetProfile()
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var profile = await _context.SkinProfiles
                .FirstOrDefaultAsync(p => p.UserId == userId);

            if (profile == null) return NotFound("Profile not found.");

            return Ok(new SkinProfileDto
            {
                SkinType = profile.SkinType,
                SensitivityLevel = profile.SensitivityLevel,
                Concerns = profile.Concerns?.Split(',').ToList() ?? new List<string>(),
                Notes = profile.Notes
            });
        }

        [HttpPost]
        public async Task<ActionResult<SkinProfileDto>> CreateProfile(CreateProfileRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var existingProfile = await _context.SkinProfiles
                .AnyAsync(p => p.UserId == userId);

            if (existingProfile) return BadRequest("Profile already exists. Use PUT to update.");

            var profile = new SkinProfile
            {
                UserId = userId.Value,
                SkinType = request.SkinType,
                SensitivityLevel = request.SensitivityLevel,
                Concerns = string.Join(',', request.Concerns),
                Notes = request.Notes,
                CreatedAt = DateTime.UtcNow,
                UpdatedAt = DateTime.UtcNow
            };

            _context.SkinProfiles.Add(profile);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetProfile), new SkinProfileDto
            {
                SkinType = profile.SkinType,
                SensitivityLevel = profile.SensitivityLevel,
                Concerns = request.Concerns,
                Notes = profile.Notes
            });
        }

        [HttpPut]
        public async Task<IActionResult> UpdateProfile(UpdateProfileRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var profile = await _context.SkinProfiles
                .FirstOrDefaultAsync(p => p.UserId == userId);

            if (profile == null) return NotFound("Profile not found.");

            profile.SkinType = request.SkinType;
            profile.SensitivityLevel = request.SensitivityLevel;
            profile.Concerns = string.Join(',', request.Concerns);
            profile.Notes = request.Notes;
            profile.UpdatedAt = DateTime.UtcNow;

            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}
