using MesajilApi.DTOs.Autenticador;
using MesajilApi.DTOs.Usuario;

namespace MesajilApi.Services
{
    public interface IAuthService
    {
        Task<LoginResponseDto?> LoginAsync(UsuarioLoginDto dto);
    }
}
