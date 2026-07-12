using MesajilApi.DTOs.Usuario;
using MesajilApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthService _authService;
        public AuthController(IAuthService authService)
        {
            _authService = authService;
        }
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] UsuarioLoginDto dto)
        {
            var resultado = await _authService.LoginAsync(dto);
            if (resultado == null)
            {
                return Unauthorized(new
                {
                    mensaje = "Correo o contraseña incorrectos."
                });
            }
            return Ok(resultado);
        }
    }
}
