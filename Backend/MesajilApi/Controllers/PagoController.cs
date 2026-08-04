using MesajilApi.DTOs.Error;
using MesajilApi.DTOs.Pago;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace MesajilApi.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class PagoController : ControllerBase
    {
        private readonly IPagoService _pagoService;
        public PagoController(IPagoService pagoService)
        {
            _pagoService = pagoService;
        }
        [HttpPost]
        public async Task<IActionResult> ProcesarPago(PagoRequestDto dto)
        {
            try
            { 
                var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
                var resultado = await _pagoService.ProcesarPagoAsync(idUsuario, dto);
                return Ok(resultado);
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponseDto
                {
                    Mensaje = ex.Message
                });
            }
        }
    }
}
