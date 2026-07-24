using MesajilApi.DTOs.DetallePedido;

namespace MesajilApi.Services
{
    public interface IDetallePedidoService
    {
        Task<List<DetallePedidoResponseDto>> ObtenerTodosAsync();
        Task<DetallePedidoResponseDto?> ObtenerPorIdAsync(int id);
        Task<DetallePedidoResponseDto> CrearAsync(DetallePedidoCreateDto dto);
        Task<DetallePedidoResponseDto?> ActualizarAsync(int id, DetallePedidoUpdateDto dto);
        Task<bool> EliminarAsync(int id);
    }
}
