using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface IDetallePedidoRepository
    {
        Task<List<DetallePedido>> ObtenerTodosAsync();
        Task<DetallePedido?> ObtenerPorIdAsync(int id);
        Task<DetallePedido> CrearAsync(DetallePedido detallePedido);
        Task<DetallePedido> ActualizarAsync(DetallePedido detallePedido);
        Task<bool> EliminarAsync(int id);
    }
}
