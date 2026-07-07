using MesajilApi.Models;
namespace MesajilApi.Services
{
    public interface IRolService
    {
        Task<IEnumerable<Rol>> ObtenerTodosAsync();
        Task<Rol?> ObtenerPorIdAsync(int id);
        Task<Rol> CrearAsync(Rol rol);
        Task ActualizarAsync(Rol rol);
        Task EliminarAsync(int id);
    }
}
