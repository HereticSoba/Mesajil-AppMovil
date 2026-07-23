using MesajilApi.Services;
using MesajilApi.DTOs.Producto;
using Microsoft.AspNetCore.Mvc;

namespace MesajilApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductoController : ControllerBase
    {
        private readonly IProductoService _productoService;
        public ProductoController(IProductoService productoService)
        {
            _productoService = productoService;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<ProductoResponseDto>>> ObtenerTodos()
        {
            var productos = await _productoService.ObtenerTodosAsync();
            return Ok(productos);
        }
        [HttpGet("{id}")]
        public async Task<ActionResult<ProductoResponseDto>> ObtenerPorId(int id)
        {
            var producto = await _productoService.ObtenerPorIdAsync(id);
            if(producto == null)
                return NotFound();
            return Ok(producto);
        }
        [HttpPost]
        public async Task<ActionResult<ProductoResponseDto>> Crear(ProductoCreateDto producto)
        {
            var nuevoProducto = await _productoService.CrearAsync(producto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = nuevoProducto.IdProducto },
                nuevoProducto);
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            await _productoService.EliminarAsync(id);
            return NoContent();
        }
    }
}
