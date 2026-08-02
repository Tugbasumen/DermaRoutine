namespace DermaRoutine.Api.DTOs
{
    public class IngredientExplanationRequest
    {
        public string IngredientName { get; set; } = string.Empty;
    }

    public class IngredientExplanationResponse
    {
        public string IngredientName { get; set; } = string.Empty;
        public string Summary { get; set; } = string.Empty;
        public List<string> CommonUses { get; set; } = new();
        public List<string> Considerations { get; set; } = new();
        public string Disclaimer { get; set; } = "This information is educational and is not medical advice.";
    }
}
