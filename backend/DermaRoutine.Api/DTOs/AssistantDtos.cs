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

    public class ProductAnalysisRequest
    {
        public List<string> Ingredients { get; set; } = new();
    }

    public class ProductAnalysisResponse
    {
        public int SuitabilityScore { get; set; }
        public string Summary { get; set; } = string.Empty;
        public List<string> KeyBenefits { get; set; } = new();
        public List<string> PotentialRisks { get; set; } = new();
    }

    public class GeneralChatRequest
    {
        public string Message { get; set; } = string.Empty;
    }

    public class GeneralChatResponse
    {
        public string Response { get; set; } = string.Empty;
    }
}
