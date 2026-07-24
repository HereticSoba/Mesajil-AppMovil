using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface IDetalleCarritoRepository
    {
        Task<List<DetalleCarrito>> ObtenerTodosAsync();
        Task<DetalleCarrito?> ObtenerPorIdAsync(int id);
        Task<DetalleCarrito> CrearAsync(DetalleCarrito detalle);
        Task<DetalleCarrito> ActualizarAsync(DetalleCarrito detalle);
        Task<bool> EliminarAsync(int id);
    }
}
