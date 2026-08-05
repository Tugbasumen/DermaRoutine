namespace DermaRoutine.Api.DTOs
{
    public class ProgressSummaryDto
    {
        public double MorningCompletionRate { get; set; }
        public double EveningCompletionRate { get; set; }
        public int CurrentStreak { get; set; }
        public List<MoodStatDto> MoodCounts { get; set; } = new();
    }

    public class MoodStatDto
    {
        public string Mood { get; set; } = string.Empty;
        public int Count { get; set; }
    }
}
