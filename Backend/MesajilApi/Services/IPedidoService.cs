using MesajilApi.DTOs.Pedido;

namespace MesajilApi.Services
{
    public interface IPedidoService
    {
        Task<List<PedidoResponseDto>> ObtenerTodosAsync();
        Task<PedidoResponseDto?> ObtenerPorIdAsync(int id);
        Task<PedidoResponseDto> CrearAsync(PedidoCreateDto dto);
        Task<PedidoResponseDto?> ActualizarAsync(int id, PedidoUpdateDto dto);
        Task<bool> EliminarAsync(int id);
    }
}
