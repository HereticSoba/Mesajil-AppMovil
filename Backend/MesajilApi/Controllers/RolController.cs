using MesajilApi.Models;
using MesajilApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RolController : ControllerBase
    {
        private readonly IRolService _rolService;
        public RolController(IRolService rolService)
        {
            _rolService = rolService;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Rol>>> ObtenerTodos()
        {
            var roles = await _rolService.ObtenerTodosAsync();
            return Ok(roles);
        }
        [HttpGet("{id}")]
        public async Task<ActionResult<Rol>> ObtenerPorId(int id)
        {
            var rol = await _rolService.ObtenerPorIdAsync(id);
            if (rol == null)
            
                return NotFound();

                return Ok(rol);
        }
        [HttpPost]
        public async Task<ActionResult<Rol>> Crear(Rol rol)
        {
            var nuevoRol = await _rolService.CrearAsync(rol);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = nuevoRol.IdRol },
                nuevoRol
                );
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, Rol rol)
        {
            if (id != rol.IdRol)
                return BadRequest();

            await _rolService.ActualizarAsync(rol);
            return NoContent();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            await _rolService.EliminarAsync(id);
            return NoContent();
        }
    }
}

