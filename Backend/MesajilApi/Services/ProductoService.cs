using MesajilApi.DTOs.Producto;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class ProductoService : IProductoService
    {
        private readonly IProductoRepository _productoRepository;
        public ProductoService(IProductoRepository productoRepository)
        {
            _productoRepository = productoRepository;
        }
        public async Task<IEnumerable<ProductoResponseDto>> ObtenerTodosAsync()
        {
            var productos = await _productoRepository.ObtenerTodosAsync();
            return ProductoMapper.ToResponseDtoList(productos);
        }
        public async Task<ProductoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var producto = await _productoRepository.ObtenerPorIdAsync(id);
            if (producto == null)
                return null;
            return ProductoMapper.ToResponseDto(producto);
        }
        public async Task<ProductoResponseDto> CrearAsync(ProductoCreateDto dto)
        {
            var entidad = ProductoMapper.ToEntity(dto);
            entidad.FechaRegistro = DateTime.Now;
            entidad.Estado = true;
            var nuevoProducto = await _productoRepository.CrearAsync(entidad);
            return ProductoMapper.ToResponseDto(nuevoProducto);
        }
        public async Task ActualizarAsync(ProductoUpdateDto dto)
        {
            var productoExistente =
                await _productoRepository.ObtenerPorIdAsync(dto.IdProducto);

            if (productoExistente == null)
                throw new Exception("El producto no existe.");

            productoExistente.IdCategoria = dto.IdCategoria;
            productoExistente.Nombre = dto.Nombre;
            productoExistente.Descripcion = dto.Descripcion;
            productoExistente.Marca = dto.Marca;
            productoExistente.Modelo = dto.Modelo;
            productoExistente.Precio = dto.Precio;

            await _productoRepository.ActualizarAsync(productoExistente);
        }
        public async Task EliminarAsync(int id)
        {
            await _productoRepository.EliminarAsync(id);
        }
    }
}
