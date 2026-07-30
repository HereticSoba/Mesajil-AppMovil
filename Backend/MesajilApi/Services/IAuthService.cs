using MesajilApi.DTOs.Autenticacion;
using MesajilApi.DTOs.Usuario;

namespace MesajilApi.Services
{
    public interface IAuthService
    {
        Task<LoginResponseDto?> LoginAsync(UsuarioLoginDto dto);
        Task<RegistroResponseDto> RegistrarAsync(UsuarioRegistroDto dto);
    }
}
