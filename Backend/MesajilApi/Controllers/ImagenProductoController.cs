using MesajilApi.DTOs.ImagenProducto;
using Microsoft.AspNetCore.Mvc;
using MesajilApi.Services;

namespace MesajilApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ImagenProductoController : ControllerBase
    {
        private readonly IImagenProductoService _imagenProductoService;
        public ImagenProductoController(IImagenProductoService imagenProductoService)
        {
            _imagenProductoService = imagenProductoService;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<ImagenProductoResponseDto>>> ObtenerTodos()
        {
            var imagenes = await _imagenProductoService.ObtenerTodosAsync();
            return Ok(imagenes);
        }
        [HttpGet("{id}")]
        public async Task<ActionResult<ImagenProductoResponseDto>> ObtenerPorId(int id)
        {
            var imagen = await _imagenProductoService.ObtenerPorIdAsync(id);
            if (imagen == null)
                return NotFound();
            return Ok(imagen);
        }
        [HttpPost]
        public async Task<ActionResult<ImagenProductoResponseDto>> Crear([FromForm] ImagenProductoCreateDto dto)
        {
            var nuevaImagen = await _imagenProductoService.CrearAsync(dto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = nuevaImagen.IdImagen },
                nuevaImagen);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, [FromForm]ImagenProductoUpdateDto dto)
        {
            if (id != dto.IdImagen)
                return BadRequest();
            await _imagenProductoService.ActualizarAsync(dto);
            return NoContent();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            await _imagenProductoService.EliminarAsync(id);
            return NoContent();
        }
    }
}
