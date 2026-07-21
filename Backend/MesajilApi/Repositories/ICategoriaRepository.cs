using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface ICategoriaRepository
    {
        Task<IEnumerable<Categoria>> ObtenerTodosAsync();
        Task<Categoria?> ObtenerPorIdAsync(int id);
        Task<Categoria> CrearAsync(Categoria categoria);
        Task ActualizarAsync(Categoria categoria);
        Task EliminarAsync(int id);
    }
}
