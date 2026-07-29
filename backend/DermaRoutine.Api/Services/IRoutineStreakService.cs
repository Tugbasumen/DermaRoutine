using DermaRoutine.Api.DTOs;

namespace DermaRoutine.Api.Services
{
    public interface IRoutineStreakService
    {
        Task<StreakDto> GetCurrentStreakAsync(int userId);
    }
}
