using Microsoft.AspNetCore.Mvc;

namespace DermaRoutine.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class HealthController : ControllerBase
    {
        [HttpGet]
        public IActionResult Get()
        {
            return Ok(new
            {
                status = "ok",
                service = "DermaRoutine API",
                timestamp = DateTime.UtcNow
            });
        }
    }
}
