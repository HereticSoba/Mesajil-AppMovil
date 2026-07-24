using MesajilApi.DTOs.Carrito;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class CarritoController : ControllerBase
    {
        private readonly IcarritoService _service;
        public CarritoController(IcarritoService service)
        {
            _service = service;
        }
        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var carritos = await _service.ObtenerTodosAsync();
            return Ok(carritos);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> ObtenerPorId(int id)
        {
            var carrito = await _service.ObtenerPorIdAsync(id);
            if(carrito == null)
                return NotFound();
            return Ok(carrito);
        }
        [HttpPost]
        public async Task<IActionResult> Crear(CarritoCreateDto dto)
        {
            var carrito = await _service.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = carrito.IdCarrito },
                carrito);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, CarritoUpdateDto dto)
        {
            var carrito = await _service.ActualizarAsync(id, dto);
            if(carrito == null)
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