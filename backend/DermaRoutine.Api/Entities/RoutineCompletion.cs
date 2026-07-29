namespace DermaRoutine.Api.Entities
{
    public class RoutineCompletion
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int RoutineId { get; set; }

        public DateTime CompletedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public User User { get; set; } = null!;
        public Routine Routine { get; set; } = null!;
    }
}
