using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.DTOs
{
    public class RoutineDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public RoutineType RoutineType { get; set; }
        public bool IsActive { get; set; }
        public int ItemCount { get; set; }
        public bool IsCompletedToday { get; set; }
    }

    public class RoutineDetailDto : RoutineDto
    {
        public List<RoutineItemDto> Items { get; set; } = new();
    }

    public class RoutineItemDto
    {
        public int Id { get; set; }
        public int ProductId { get; set; }
        public string ProductName { get; set; } = string.Empty;
        public string? ProductBrand { get; set; }
        public string? ProductImageUrl { get; set; }
        public int StepOrder { get; set; }
        public string? Notes { get; set; }
    }

    public class CreateRoutineRequest
    {
        public string Name { get; set; } = string.Empty;
        public RoutineType RoutineType { get; set; }
    }

    public class UpdateRoutineRequest
    {
        public string Name { get; set; } = string.Empty;
        public RoutineType RoutineType { get; set; }
        public bool IsActive { get; set; }
    }

    public class AddRoutineItemRequest
    {
        public int ProductId { get; set; }
        public int StepOrder { get; set; }
        public string? Notes { get; set; }
    }

    public class StreakDto
    {
        public int CurrentStreak { get; set; }
        public bool TodayCompleted { get; set; }
    }
}
