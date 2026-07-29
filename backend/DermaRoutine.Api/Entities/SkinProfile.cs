using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.Entities
{
    public class SkinProfile
    {
        public int Id { get; set; }
        public int UserId { get; set; }

        public SkinType SkinType { get; set; }
        public SensitivityLevel SensitivityLevel { get; set; }
        public string? Concerns { get; set; }
        public string? Notes { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public User User { get; set; } = null!;
    }
}
