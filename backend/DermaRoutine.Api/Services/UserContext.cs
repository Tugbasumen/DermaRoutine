using System.Security.Claims;
using DermaRoutine.Api.Data;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Services
{
    public class UserContext : IUserContext
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private readonly AppDbContext _dbContext;

        public UserContext(IHttpContextAccessor httpContextAccessor, AppDbContext dbContext)
        {
            _httpContextAccessor = httpContextAccessor;
            _dbContext = dbContext;
        }

        public string? FirebaseUid => _httpContextAccessor.HttpContext?.User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

        public async Task<int?> GetUserIdAsync()
        {
            var uid = FirebaseUid;
            if (string.IsNullOrEmpty(uid)) return null;

            var user = await _dbContext.Users.FirstOrDefaultAsync(u => u.FirebaseUid == uid);
            if (user == null)
            {
                // Synchronize user if not exists
                var email = _httpContextAccessor.HttpContext?.User.FindFirst("email")?.Value;
                var name = _httpContextAccessor.HttpContext?.User.FindFirst("name")?.Value;

                user = new Entities.User
                {
                    FirebaseUid = uid,
                    Email = email ?? "unknown@example.com",
                    DisplayName = name,
                    CreatedAt = DateTime.UtcNow,
                    UpdatedAt = DateTime.UtcNow
                };

                _dbContext.Users.Add(user);
                await _dbContext.SaveChangesAsync();
            }

            return user.Id;
        }
    }
}
