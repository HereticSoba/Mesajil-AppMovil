using MesajilApi.DTOs.Usuario;

namespace MesajilApi.Services
{
    public interface IUsuarioService
    {
        Task<IEnumerable<UsuarioResponseDto>> ObtenerTodosAsync();
        Task<UsuarioResponseDto?> ObtenerPorIdAsync(int id);
        Task<UsuarioResponseDto> CrearAsync(UsuarioCreateDto dto);
        Task ActualizarAsync(UsuarioUpdateDto dto);
        Task EliminarAsync(int id);
    }
}
