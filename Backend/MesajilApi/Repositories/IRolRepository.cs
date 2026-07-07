using MesajilApi.Models;
namespace MesajilApi.Repositories
{
    public interface IRolRepository
    {
        Task<IEnumerable<Rol>> ObtenerTodosAsync();
        Task<Rol?> ObtenerPorIdAsync(int id);
        Task<Rol?> CrearAsync(Rol rol);
        Task ActualizarAsync(Rol rol);
        Task EliminarAsync(int id);
    }
}
