using MesajilApi.DTOs.Producto;

namespace MesajilApi.Services
{
    public interface IProductoService
    {
        Task<IEnumerable<ProductoResponseDto>> ObtenerTodosAsync();
        Task<ProductoResponseDto?> ObtenerPorIdAsync(int id);
        Task<ProductoResponseDto> CrearAsync(ProductoCreateDto producto);
        Task ActualizarAsync(ProductoUpdateDto producto);
        Task EliminarAsync(int id);
    }
}
