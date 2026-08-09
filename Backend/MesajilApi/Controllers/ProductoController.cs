using MesajilApi.Services;
using MesajilApi.DTOs.Producto;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

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
            if (producto == null)
                return NotFound();
            return Ok(producto);
        }
        [Authorize]
        [HttpPost]
        public async Task<ActionResult<ProductoResponseDto>> Crear(ProductoCreateDto producto)
        {
            if(!EsAdministrador())
                return Forbid();
            var nuevoProducto = await _productoService.CrearAsync(producto);
            return CreatedAtAction(
                nameof(ObtenerPorId),
                new { id = nuevoProducto.IdProducto },
                nuevoProducto);
        }

        [Authorize]
        [HttpPut("{id}")]
        public async Task<IActionResult> Actualizar(
            int id,
            ProductoUpdateDto dto)
        {
            if(!EsAdministrador())
                return Forbid();
            if (id != dto.IdProducto)
                return BadRequest();
            try
            {
                await _productoService.ActualizarAsync(dto);
                return NoContent();
            }catch(Exception ex)
            {
                return NotFound(new
                {
                    mensaje = ex.Message
                });
            }
        }

        [Authorize]
        [HttpDelete("{id}")]
        public async Task<IActionResult> Eliminar(int id)
        {
            if(!EsAdministrador())
                return Forbid();
            await _productoService.EliminarAsync(id);
            return NoContent();
        }

        private bool EsAdministrador()
        {
            var idRol = User.FindFirst("IdRol")?.Value;
            return int.TryParse(idRol, out int rol) && rol == 1;
        }
    }
}
