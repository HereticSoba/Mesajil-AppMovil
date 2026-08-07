using MesajilApi.DTOs.Pedido;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using MesajilApi.DTOs.Error;

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
        [HttpGet("mis-pedidos")]
        public async Task<IActionResult> ObtenerMisPedidos()
        {
            var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
            var pedidos = await _service.ObtenerMisPedidosAsync(idUsuario);
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
        [HttpPost("finalizar")]
        public async Task<IActionResult> FinalizarCompra(
            [FromBody] FinalizarCompraDto dto)
        {
            try
            {
                var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
                var resultado = await _service.FinalizarCompraAsync(idUsuario, dto);
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
        [HttpPut("{id}/cancelar")]
        public async Task<IActionResult> CancelarPedido(int id)
        {
            try
            {
                var idUsuario = int.Parse(
                    User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
                await _service.CancelarPedidoAsync(idUsuario, id);
                return NoContent();
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponseDto
                {
                    Mensaje = ex.Message
                });
            }
        }
        [HttpGet("{id}/detalle")]
        public async Task<IActionResult>ObtenerDetalle(int id)
        {
            var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
            var detalle = await _service.ObtenerDetallePedidoAsync(idUsuario, id);
            if(detalle == null)
                return NotFound();
            return Ok(detalle);
        }
    }
}