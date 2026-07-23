using MesajilApi.DTOs.Categoria;
using MesajilApi.Services;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoriaController : ControllerBase
    {
        private readonly ICategoriaService _categoriaService;
        public CategoriaController(ICategoriaService categoriaService)
        {
            _categoriaService = categoriaService;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<CategoriaResponseDto>>> ObtenerTodos()
        {
            var categorias = await _categoriaService.ObtenerTodosAsync();
            return Ok(categorias);
        }
        [HttpGet("{id}")]
        public async Task<ActionResult<CategoriaResponseDto>> ObtenerPorId(int id)
        {
            var categoria = await _categoriaService.ObtenerPorIdAsync(id);
            if (categoria == null)
                return NotFound();
            return Ok(categoria);
        }
        [HttpPost]
        public async Task<ActionResult<CategoriaResponseDto>> Crear(CategoriaCreateDto categoria)
        {
            var nuevaCategoria = await _categoriaService.CrearAsync(categoria);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new {id = nuevaCategoria.IdCategoria},
                nuevaCategoria
                );
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(int id, CategoriaUpdateDto dto)
        {
            if(id != dto.IdCategoria)
                return BadRequest();

            await _categoriaService.ActualizarAsync(dto);
            return NoContent();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            await _categoriaService.EliminarAsync(id);
            return NoContent();
        }
    }
}
