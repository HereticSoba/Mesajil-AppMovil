using MesajilApi.DTOs.Pedido;
using MesajilApi.Repositories;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class PedidoController : ControllerBase
    {
        private readonly IPedidoService _service;
        public PedidoController(IPedidoService service)
        {
            _service = service;
        }
        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var pedidos = await _service.ObtenerTodosAsync();
            return Ok(pedidos);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> ObtenerPorId(int id)
        {
            var pedido = await _service.ObtenerPorIdAsync(id);
            if(pedido == null)
                return NotFound();
            return Ok(pedido);
        }
        [HttpPost]
        public async Task<IActionResult> Crear(PedidoCreateDto dto)
        {
            var pedido = await _service.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = pedido.IdPedido },
                pedido);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, PedidoUpdateDto dto)
        {
            var pedido = await _service.ActualizarAsync(id, dto);
            if(pedido == null)
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