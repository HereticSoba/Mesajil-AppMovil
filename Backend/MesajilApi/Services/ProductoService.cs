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
            var entidad = ProductoMapper.ToEntity(dto);
            var productoExistente = await _productoRepository.ObtenerPorIdAsync(dto.IdProducto);
            if (productoExistente == null)
                throw new Exception("El producto no existe.");
            entidad.FechaRegistro = productoExistente.FechaRegistro;
            entidad.Estado = productoExistente.Estado;
            await _productoRepository.ActualizarAsync(entidad);
        }
        public async Task EliminarAsync(int id)
        {
            await _productoRepository.EliminarAsync(id);
        }
    }
}
