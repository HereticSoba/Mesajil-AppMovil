using BCrypt.Net;
using MesajilApi.DTOs.Autenticador;
using MesajilApi.DTOs.Usuario;
using MesajilApi.Repositories;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.Extensions.Configuration;

namespace MesajilApi.Services
{
    public class AuthService : IAuthService
    {
        private readonly IUsuarioRepository _usuarioRepository;
        private readonly IConfiguration _configuration;
        public AuthService(IUsuarioRepository usuarioRepository, IConfiguration configuration)
        {
            _usuarioRepository = usuarioRepository;
            _configuration = configuration;
        }
        public async Task<LoginResponseDto?> LoginAsync(UsuarioLoginDto dto)
        {
            var usuario = await _usuarioRepository.ObtenerPorCorreoAsync(dto.Correo);
            if (usuario == null)
            
                return null;

                bool passwordCorrecta = BCrypt.Net.BCrypt.Verify(
                    dto.Contrasena, usuario.Contrasena);
                if (!passwordCorrecta)
                    return null;

                var claims = new[]
                {
                    new Claim(JwtRegisteredClaimNames.Sub, usuario.IdUsuario.ToString()),
                    new Claim(JwtRegisteredClaimNames.Email, usuario.Correo),
                    new Claim("IdRol", usuario.IdRol.ToString()),
                    new Claim("Nombres", usuario.Nombres)
                };
                var key = new SymmetricSecurityKey(
                    Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]!));
                var credentials = new SigningCredentials(
                    key, SecurityAlgorithms.HmacSha256);
                var token = new JwtSecurityToken(
                    issuer: _configuration["Jwt:Issuer"],
                    audience: _configuration["Jwt:Audience"],
                    claims: claims,
                    expires: DateTime.Now.AddMinutes(Convert.ToDouble(_configuration["Jwt:DurationInMinutes"])),
                    signingCredentials: credentials);

                return new LoginResponseDto
                {
                    Token = new JwtSecurityTokenHandler().WriteToken(token),
                    IdUsuario = usuario.IdUsuario,
                    Nombres = usuario.Nombres,
                    Correo = usuario.Correo,
                    IdRol = usuario.IdRol
                };
        }
    }
}