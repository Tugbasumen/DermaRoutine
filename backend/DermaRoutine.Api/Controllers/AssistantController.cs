using System.Text.Json;
using DermaRoutine.Api.DTOs;
using DermaRoutine.Api.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace DermaRoutine.Api.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class AssistantController : ControllerBase
    {
        private readonly IGeminiService _geminiService;

        public AssistantController(IGeminiService geminiService)
        {
            _geminiService = geminiService;
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

                // Clean the response if it has markdown formatting
                var cleanedResponse = aiResponse.Replace("```json", "").Replace("```", "").Trim();

                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var result = JsonSerializer.Deserialize<IngredientExplanationResponse>(cleanedResponse, options);

                if (result == null) return StatusCode(502, "Failed to parse AI response.");

                return Ok(result);
            }
            catch (Exception)
            {
                // In production, log the exception
                return StatusCode(500, "An error occurred while processing the AI request.");
            }
        }
    }
}
