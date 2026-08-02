namespace DermaRoutine.Api.DTOs
{
    public class SkinPhotoDto
    {
        public int Id { get; set; }
        public string ImageUrl { get; set; } = string.Empty;
        public DateTime TakenAt { get; set; }
        public int Rating { get; set; }
        public string? Notes { get; set; }
    }

    public class CreateSkinPhotoRequest
    {
        public string ImageUrl { get; set; } = string.Empty;
        public DateTime TakenAt { get; set; }
        public int Rating { get; set; }
        public string? Notes { get; set; }
    }

    public class UpdateSkinPhotoRequest
    {
        public DateTime TakenAt { get; set; }
        public int Rating { get; set; }
        public string? Notes { get; set; }
    }
}
