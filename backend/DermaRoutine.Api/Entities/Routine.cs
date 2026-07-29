using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.Entities
{
    public class Routine
    {
        public int Id { get; set; }
        public int UserId { get; set; }

        public string Name { get; set; } = string.Empty;
        public RoutineType RoutineType { get; set; }
        public bool IsActive { get; set; } = true;

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public User User { get; set; } = null!;
        public ICollection<RoutineItem> RoutineItems { get; set; } = new List<RoutineItem>();
        public ICollection<RoutineCompletion> Completions { get; set; } = new List<RoutineCompletion>();
    }
}
