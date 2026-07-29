namespace DermaRoutine.Api.Entities
{
    public class SkinPhoto
    {
        public int Id { get; set; }
        public int UserId { get; set; }

        public string ImageUrl { get; set; } = string.Empty;
        public DateTime TakenAt { get; set; }
        public int Rating { get; set; } // 1-5
        public string? Notes { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public User User { get; set; } = null!;
    }
}
