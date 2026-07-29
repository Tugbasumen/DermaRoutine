using System.ComponentModel.DataAnnotations;

namespace DermaRoutine.Api.Entities
{
    public class User
    {
        public int Id { get; set; }

        [Required]
        public string FirebaseUid { get; set; } = string.Empty;

        [Required]
        public string Email { get; set; } = string.Empty;

        public string? DisplayName { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public SkinProfile? SkinProfile { get; set; }
        public ICollection<Product> Products { get; set; } = new List<Product>();
        public ICollection<Routine> Routines { get; set; } = new List<Routine>();
        public ICollection<RoutineCompletion> RoutineCompletions { get; set; } = new List<RoutineCompletion>();
        public ICollection<SkinPhoto> SkinPhotos { get; set; } = new List<SkinPhoto>();
        public ICollection<JournalEntry> JournalEntries { get; set; } = new List<JournalEntry>();
    }
}
