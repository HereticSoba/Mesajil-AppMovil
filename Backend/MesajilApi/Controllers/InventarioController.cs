using MesajilApi.DTOs.Inventario;
using MesajilApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class InventarioController : ControllerBase
    {
        private readonly IInventarioService _inventarioService;
        public InventarioController(IInventarioService inventarioService)
        {
            _inventarioService = inventarioService;
        }
        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var inventarios = await _inventarioService.ObtenerTodosAsync();
            return Ok(inventarios);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> ObtenerPorId(int id)
        {
            var inventario = await _inventarioService.ObtenerPorIdAsync(id);
            if(inventario == null)
                return NotFound();
            return Ok(inventario);
        }
        [HttpPost]
        public async Task<IActionResult> Crear(InventarioCreateDto dto)
        {
            var inventario = await _inventarioService.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = inventario.IdInventario },
                inventario);
        }
        [HttpPut]
        public async Task<IActionResult> Actualizar(InventarioUpdateDto dto)
        {
            await _inventarioService.ActualizarAsync(dto);
            return NoContent();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            await _inventarioService.EliminarAsync(id);
            return NoContent();
        }
    }
}
