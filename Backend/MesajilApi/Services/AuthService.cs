using BCrypt.Net;
using MesajilApi.DTOs.Autenticador;
using MesajilApi.DTOs.Usuario;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class AuthService : IAuthService
    {
        private readonly IUsuarioRepository _usuarioRepository;
        public AuthService(IUsuarioRepository usuarioRepository)
        {
            _usuarioRepository = usuarioRepository;
        }
        public async Task<LoginResponseDto?> LoginAsync(UsuarioLoginDto dto)
        {
            throw new NotImplementedException();
        }
    }
}
