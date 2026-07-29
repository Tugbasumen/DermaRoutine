namespace DermaRoutine.Api.Entities
{
    public class RoutineItem
    {
        public int Id { get; set; }
        public int RoutineId { get; set; }
        public int ProductId { get; set; }

        public int StepOrder { get; set; }
        public string? Notes { get; set; }

        // Navigation Properties
        public Routine Routine { get; set; } = null!;
        public Product Product { get; set; } = null!;
    }
}
