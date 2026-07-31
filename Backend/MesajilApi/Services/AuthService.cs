using MesajilApi.DTOs.Autenticacion;
using MesajilApi.DTOs.Usuario;
using MesajilApi.Repositories;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

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
            if (!usuario.Estado)
            {
                throw new Exception("La cuenta se encuentra desactivada.");
            }

            bool passwordCorrecta = BCrypt.Net.BCrypt.Verify(
                dto.Contrasena, usuario.Contrasena);
            if(!passwordCorrecta)
            {
                throw new Exception("Correo o contraseña incorrectos.");
            }

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
        public async Task<RegistroResponseDto> RegistrarAsync(UsuarioRegistroDto dto)
        {
            var usuarioExistente = await _usuarioRepository.ObtenerPorCorreoAsync(dto.Correo);
            if (usuarioExistente != null)
            {
                if(!usuarioExistente.Estado)
                {
                    throw new Exception(
                        "Ya existe una cuenta asociada a este correo, en estado desactivado. Comuníquese con soporte."
                        );
                }
                throw new Exception("El correo electrónico ya está registrado.");
            }
            var usuario = new Models.Usuario
            {
                IdRol = 2,
                Nombres = dto.Nombres,
                Apellidos = dto.Apellidos,
                Correo = dto.Correo,
                Contrasena = BCrypt.Net.BCrypt.HashPassword(dto.Contrasena),
                Telefono = dto.Telefono,
                Direccion = dto.Direccion,
                FechaRegistro = DateTime.Now,
                Estado = true
            };
            var creado = await _usuarioRepository.CrearAsync(usuario);
            return new RegistroResponseDto
            {
                IdUsuario = creado.IdUsuario,
                Nombres = creado.Nombres,
                Correo = creado.Correo,
                Mensaje = "Usuario registrado exitosamente."
            };
        }
    }
}