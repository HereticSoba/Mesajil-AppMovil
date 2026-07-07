using MesajilApi.DTOs.Rol;
namespace MesajilApi.Services
{
    public interface IRolService
    {
        Task<IEnumerable<RolResponseDto>> ObtenerTodosAsync();
        Task<RolResponseDto?> ObtenerPorIdAsync(int id);
        Task<RolResponseDto> CrearAsync(RolCreateDto rol);
        Task ActualizarAsync(RolUpdateDto rol);
        Task EliminarAsync(int id);
    }
}
