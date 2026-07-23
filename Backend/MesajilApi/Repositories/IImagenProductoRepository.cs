using MesajilApi.Models;

namespace MesajilApi.Repositories
{
    public interface IImagenProductoRepository
    {
        Task<IEnumerable<ImagenProducto>> ObtenerTodosAsync();
        Task<ImagenProducto?> ObtenerPorIdAsync(int id);
        Task<ImagenProducto> CrearAsync(ImagenProducto imagenProducto);
        Task ActualizarAsync(ImagenProducto imagenProducto);
        Task EliminarAsync(int id);
    }
}
