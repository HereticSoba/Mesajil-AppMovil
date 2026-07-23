using MesajilApi.DTOs.Inventario;

namespace MesajilApi.Services
{
    public interface IInventarioService
    {
        Task<IEnumerable<InventarioResponseDto>> ObtenerTodosAsync();
        Task<InventarioResponseDto?> ObtenerPorIdAsync(int id);
        Task<InventarioResponseDto> CrearAsync(InventarioCreateDto dto);
        Task ActualizarAsync(InventarioUpdateDto dto);
        Task EliminarAsync(int id);
    }
}
