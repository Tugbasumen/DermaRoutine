using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.DTOs
{
    public class SkinProfileDto
    {
        public SkinType SkinType { get; set; }
        public SensitivityLevel SensitivityLevel { get; set; }
        public List<string> Concerns { get; set; } = new List<string>();
        public string? Notes { get; set; }
    }

    public class CreateProfileRequest
    {
        public SkinType SkinType { get; set; }
        public SensitivityLevel SensitivityLevel { get; set; }
        public List<string> Concerns { get; set; } = new List<string>();
        public string? Notes { get; set; }
    }

    public class UpdateProfileRequest
    {
        public SkinType SkinType { get; set; }
        public SensitivityLevel SensitivityLevel { get; set; }
        public List<string> Concerns { get; set; } = new List<string>();
        public string? Notes { get; set; }
    }
}
