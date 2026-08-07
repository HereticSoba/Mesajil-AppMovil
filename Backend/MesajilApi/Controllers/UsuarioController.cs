using MesajilApi.DTOs.Usuario;
using MesajilApi.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Diagnostics;
using MesajilApi.DTOs.Error;

namespace MesajilApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuarioController : ControllerBase
    {
        private readonly IUsuarioService _usuarioService;
        public UsuarioController(IUsuarioService usuarioService)
        {
            _usuarioService = usuarioService;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<UsuarioResponseDto>>> ObtenerTodos()
        {
            var usuarios = await _usuarioService.ObtenerTodosAsync();
            return Ok(usuarios);
        }
        [Authorize]
        [HttpGet("{id}")]
        public async Task<ActionResult<UsuarioResponseDto>> ObtenerPorId(int id)
        {
            var usuario = await _usuarioService.ObtenerPorIdAsync(id);
            if (usuario == null)
                return NotFound();
            return Ok(usuario);
        }
        [HttpPost]
        public async Task<ActionResult<UsuarioResponseDto>> Crear(UsuarioCreateDto dto)
        {
            var nuevoUsuario = await _usuarioService.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new
                {
                    id = nuevoUsuario.IdUsuario
                },
                nuevoUsuario);
        }
        [Authorize]
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, UsuarioUpdateDto dto)
        {
            if (id != dto.IdUsuario)
                return BadRequest();
            await _usuarioService.ActualizarAsync(dto);
            return NoContent();
        }
        [Authorize]
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            var idUsuarioToken = int.Parse(
                User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            var idRolToken = int.Parse(
                User.FindFirst("IdRol")!.Value);
            if (idRolToken != 1 && idUsuarioToken != id)
            {
                return StatusCode(StatusCodes.Status403Forbidden, new
                {
                    mensaje = "No tienes permisos para eliminar esta cuenta."
                });
            }
            await _usuarioService.EliminarAsync(id);
            return NoContent();
        }
        [Authorize]
        [HttpPut("mi-perfil")]
        public async Task<IActionResult> ActualizarPerfil(ActualizarPerfilDto dto)
        {
            try
            {
                var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
                await _usuarioService.ActualizarPerfilAsync(idUsuario, dto);
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
        [Authorize]
        [HttpGet("mi-perfil")]
        public async Task<ActionResult<UsuarioResponseDto>> ObtenerMiPerfil()
        {
            var idUsuario = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);
            var usuario = await _usuarioService.ObtenerMiPerfilAsync(idUsuario);
            if (usuario == null)
                return NotFound();
            return Ok(usuario);
        }
    }
}
