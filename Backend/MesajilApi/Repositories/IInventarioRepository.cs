using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface IInventarioRepository
    {
        Task<IEnumerable<Inventario>> ObtenerTodosAsync();
        Task<Inventario?> ObtenerPorIdAsync(int id);
        Task CrearAsync(Inventario inventario);
        Task ActualizarAsync(Inventario inventario);
        Task EliminarAsync(int id);
    }
}
