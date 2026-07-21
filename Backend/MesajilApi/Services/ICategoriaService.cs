using MesajilApi.DTOs.Categoria;

namespace MesajilApi.Services
{
    public interface ICategoriaService
    {
        Task<IEnumerable<CategoriaResponseDto>> ObtenerTodosAsync();
        Task<CategoriaResponseDto?> ObtenerPorIdAsync(int id);
        Task<CategoriaResponseDto> CrearAsync(CategoriaCreateDto categoria);
        Task ActualizarAsync(CategoriaUpdateDto categoria);
        Task EliminarAsync(int id);
    }
}
