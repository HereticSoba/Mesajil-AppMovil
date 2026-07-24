using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface ICarritoRepository
    {
        Task<List<Carrito>> ObtenerTodosAsync();
        Task<Carrito?> ObtenerPorIdAsync(int id);
        Task<Carrito> CrearAsync(Carrito carrito);
        Task<Carrito> ActualizarAsync(Carrito carrito);
        Task<bool> EliminarAsync(int id);
    }
}
