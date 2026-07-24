using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface IPedidoRepository
    {
        Task<List<Pedido>> ObtenerTodosAsync();
        Task<Pedido?> ObtenerPorIdAsync(int id);
        Task<Pedido> CrearAsync(Pedido pedido);
        Task<Pedido> ActualizarAsync(Pedido pedido);
        Task<bool> EliminarAsync(int id);
    }
}
