using MesajilApi.DTOs.DetallePedido;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class DetallePedidoController : ControllerBase
    {
        private readonly IDetallePedidoService _service;
        public DetallePedidoController(IDetallePedidoService service)
        {
            _service = service;
        }
        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var detalles = await _service.ObtenerTodosAsync();
            return Ok(detalles);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> ObtenerPorId(int id)
        {
            var detalle = await _service.ObtenerPorIdAsync(id);
            if (detalle == null)
                return NotFound();
            return Ok(detalle);
        }
        [HttpPost]
        public async Task<IActionResult> Crear(DetallePedidoCreateDto dto)
        {
            var detalle = await _service.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = detalle.IdDetallePedido },
                detalle);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, DetallePedidoUpdateDto dto)
        {
            var detalle = await _service.ActualizarAsync(id, dto);
            if(detalle == null)
                return NotFound();
            return NoContent();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            var eliminado = await _service.EliminarAsync(id);
            if(!eliminado)
                return NotFound();
            return NoContent();
        }
    }
}
