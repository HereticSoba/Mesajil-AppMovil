using MesajilApi.DTOs.DetalleCarrito;

namespace MesajilApi.Services
{
    public interface IDetalleCarritoService
    {
        Task<List<DetalleCarritoResponseDto>> ObtenerTodosAsync();
        Task<DetalleCarritoResponseDto?> ObtenerPorIdAsync(int id);
        Task<DetalleCarritoResponseDto> CrearAsync(DetalleCarritoCreateDto dto);
        Task<DetalleCarritoResponseDto?> ActualizarAsync(int id, DetalleCarritoUpdateDto dto);
        Task<bool> EliminarAsync(int id);
    }
}
