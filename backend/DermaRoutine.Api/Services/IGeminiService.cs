namespace DermaRoutine.Api.Services
{
    public interface IGeminiService
    {
        Task<string?> GenerateContentAsync(string prompt);
    }
}
