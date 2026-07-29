namespace DermaRoutine.Api.Services
{
    public interface IUserContext
    {
        string? FirebaseUid { get; }
        Task<int?> GetUserIdAsync();
    }
}
