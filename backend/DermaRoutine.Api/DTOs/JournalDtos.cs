namespace DermaRoutine.Api.DTOs
{
    public class CreateJournalEntryRequest
    {
        public string Content { get; set; } = string.Empty;
        public string? Mood { get; set; }
    }

    public class JournalEntryResponse
    {
        public int Id { get; set; }
        public string Content { get; set; } = string.Empty;
        public string? Mood { get; set; }
        public DateTime CreatedAt { get; set; }
    }
}
