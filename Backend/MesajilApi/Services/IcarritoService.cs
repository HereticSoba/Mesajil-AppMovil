using MesajilApi.DTOs.Carrito;

namespace MesajilApi.Services
{
    public interface IcarritoService
    {
        Task<List<CarritoResponseDto>> ObtenerTodosAsync();
        Task<CarritoResponseDto?> ObtenerPorIdAsync(int id);
        Task<CarritoResponseDto> CrearAsync(CarritoCreateDto dto);
        Task<CarritoResponseDto?> ActualizarAsync(int id, CarritoUpdateDto dto);
        Task<bool> EliminarAsync(int id);
    }
}
