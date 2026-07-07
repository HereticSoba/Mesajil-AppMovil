using MesajilApi.DTOs.Rol;
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
        public async Task<ActionResult<IEnumerable<RolResponseDto>>> ObtenerTodos()
        {
            var roles = await _rolService.ObtenerTodosAsync();
            return Ok(roles);
        }
        [HttpGet("{id}")]
        public async Task<ActionResult<RolResponseDto>> ObtenerPorId(int id)
        {
            var rol = await _rolService.ObtenerPorIdAsync(id);
            if (rol == null)
            
                return NotFound();

                return Ok(rol);
        }
        [HttpPost]
        public async Task<ActionResult<RolResponseDto>> Crear(RolCreateDto rol)
        {
            var nuevoRol = await _rolService.CrearAsync(rol);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = nuevoRol.IdRol },
                nuevoRol
                );
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, RolUpdateDto dto)
        {
            if (id != dto.IdRol)
                return BadRequest();

            await _rolService.ActualizarAsync(dto);
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

