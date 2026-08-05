namespace DermaRoutine.Api.Entities
{
    public class RoutineItemCompletion
    {
        public int Id { get; set; }
        public int RoutineItemId { get; set; }
        public int UserId { get; set; }
        public DateTime CompletedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public RoutineItem RoutineItem { get; set; } = null!;
        public User User { get; set; } = null!;
    }
}
