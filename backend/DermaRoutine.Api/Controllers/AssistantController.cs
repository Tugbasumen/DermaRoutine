using System.Text.Json;
using DermaRoutine.Api.Data;
using DermaRoutine.Api.DTOs;
using DermaRoutine.Api.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class AssistantController : ControllerBase
    {
        private readonly IGeminiService _geminiService;
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;

        public AssistantController(IGeminiService geminiService, AppDbContext context, IUserContext userContext)
        {
            _geminiService = geminiService;
            _context = context;
            _userContext = userContext;
        }

        [HttpPost("ingredient")]
        public async Task<ActionResult<IngredientExplanationResponse>> GetIngredientExplanation(IngredientExplanationRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.IngredientName)) return BadRequest("Ingredient name is required.");

            var systemPrompt = @"You are an educational skincare ingredient information assistant.
            Provide general, educational information about the requested skincare ingredient.
            Return the result ONLY as a raw JSON object with the following structure:
            {
                ""ingredientName"": ""string"",
                ""summary"": ""string (one or two sentences)"",
                ""commonUses"": [""string"", ""string""],
                ""considerations"": [""string"", ""string""],
                ""disclaimer"": ""This information is educational and is not medical advice.""
            }
            Do not: diagnose medical conditions, recommend treatment, prescribe products, guarantee results, or give individualized medical advice.
            Be concise and educational.";

            var prompt = $"{systemPrompt}\n\nIngredient: {request.IngredientName}";

            try
            {
                var aiResponse = await _geminiService.GenerateContentAsync(prompt);
                if (string.IsNullOrEmpty(aiResponse)) return StatusCode(502, "No response from AI service.");

                var cleanedResponse = aiResponse.Replace("```json", "").Replace("```", "").Trim();
                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var result = JsonSerializer.Deserialize<IngredientExplanationResponse>(cleanedResponse, options);

                return result != null ? Ok(result) : StatusCode(502, "Failed to parse AI response.");
            }
            catch (Exception)
            {
                return StatusCode(500, "An error occurred while processing the AI request.");
            }
        }

        [HttpPost("analyze-product")]
        public async Task<ActionResult<ProductAnalysisResponse>> AnalyzeProduct(ProductAnalysisRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var profile = await _context.SkinProfiles.FirstOrDefaultAsync(p => p.UserId == userId);
            if (profile == null) return BadRequest("Lütfen önce profilinizi tamamlayın.");

            var ingredientsStr = string.Join(", ", request.Ingredients);

            var systemPrompt = $@"You are an AI Skincare Expert. Your goal is to analyze a product for a specific user.
            User Profile:
            - Skin Type: {profile.SkinType}
            - Sensitivity: {profile.SensitivityLevel}
            - Concerns: {string.Join(", ", profile.Concerns)}

            Return the result ONLY as a raw JSON object with the following structure:
            {{
                ""suitabilityScore"": integer (0-100),
                ""summary"": ""string (Turkish, personalized explanation)"",
                ""keyBenefits"": [""string"", ""string""],
                ""potentialRisks"": [""string"", ""string""]
            }}
            Language: TURKISH.
            The tone should be professional and educational.
            Do not provide medical diagnosis.";

            var prompt = $"{systemPrompt}\n\nIngredients: {ingredientsStr}";

            try
            {
                var aiResponse = await _geminiService.GenerateContentAsync(prompt);
                if (string.IsNullOrEmpty(aiResponse)) return StatusCode(502, "No response from AI.");

                var cleanedResponse = aiResponse.Replace("```json", "").Replace("```", "").Trim();
                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var result = JsonSerializer.Deserialize<ProductAnalysisResponse>(cleanedResponse, options);

                return result != null ? Ok(result) : StatusCode(502, "Failed to parse analysis.");
            }
            catch (Exception)
            {
                return StatusCode(500, "An error occurred.");
            }
        }

        [HttpPost("chat")]
        public async Task<ActionResult<GeneralChatResponse>> GeneralChat(GeneralChatRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.Message)) return BadRequest("Mesaj boş olamaz.");

            var userId = await _userContext.GetUserIdAsync();
            var profile = userId != null ? await _context.SkinProfiles.FirstOrDefaultAsync(p => p.UserId == userId) : null;
            var userName = (await _context.Users.FirstOrDefaultAsync(u => u.Id == userId))?.DisplayName ?? "Kullanıcı";

            var systemPrompt = $@"You are DermaRoutine AI, a helpful and educational skincare assistant.
            The user's name is {userName}.
            {(profile != null ? $"User Skin Profile: {profile.SkinType} skin, {profile.SensitivityLevel} sensitivity." : "")}

            Instructions:
            1. Be concise, professional, and supportive.
            2. Provide EDUCATIONAL information about skincare routines, ingredients, and best practices.
            3. LANGUAGE: Always respond in TURKISH.
            4. IMPORTANT: Do not diagnose medical conditions (acne, eczema, etc.).
            5. IMPORTANT: Do not prescribe treatments or drugs.
            6. ALWAYS include a small reminder that this is not medical advice if giving specific routine suggestions.

            Goal: Answer the user's specific question about skincare.";

            var prompt = $"{systemPrompt}\n\nUser Question: {request.Message}";

            try
            {
                var aiResponse = await _geminiService.GenerateContentAsync(prompt);
                if (string.IsNullOrEmpty(aiResponse)) return StatusCode(502, "AI'dan yanıt alınamadı.");

                return Ok(new GeneralChatResponse { Response = aiResponse.Trim() });
            }
            catch (Exception)
            {
                return StatusCode(500, "Sohbet işlenirken bir hata oluştu.");
            }
        }
    }
}
