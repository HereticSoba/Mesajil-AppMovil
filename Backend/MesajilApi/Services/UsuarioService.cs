using MesajilApi.DTOs.Usuario;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class UsuarioService : IUsuarioService
    {
        private readonly IUsuarioRepository _usuarioRepository;
        public UsuarioService(IUsuarioRepository usuarioRepository)
        {
            _usuarioRepository = usuarioRepository;
        }
        public async Task<IEnumerable<UsuarioResponseDto>> ObtenerTodosAsync()
        {
            var usuarios = await _usuarioRepository.ObtenerTodosAsync();
            return UsuarioMapper.toResponseDtoList(usuarios);
        }
        public async Task<UsuarioResponseDto?> ObtenerPorIdAsync(int id)
        {
            var usuario = await _usuarioRepository.ObtenerPorIdAsync(id);
            if (usuario == null)
                return null;
            return UsuarioMapper.toResponseDto(usuario);
        }
        public async Task<UsuarioResponseDto> CrearAsync(UsuarioCreateDto dto)
        {
            var entidad = UsuarioMapper.toEntity(dto);
            var nuevoUsuario = await _usuarioRepository.CrearAsync(entidad);
            return UsuarioMapper.toResponseDto(nuevoUsuario);
        }
        public async Task ActualizarAsync(UsuarioUpdateDto dto)
        {
            var usuarioExistente =
                await _usuarioRepository.ObtenerPorIdAsync(dto.IdUsuario);
            if (usuarioExistente == null)
            {
                throw new Exception("Usuario no encontrado.");
            }

            usuarioExistente.IdRol = dto.IdRol;
            usuarioExistente.Nombres = dto.Nombres;
            usuarioExistente.Apellidos = dto.Apellidos;
            usuarioExistente.Correo = dto.Correo;
            usuarioExistente.Telefono = dto.Telefono;
            usuarioExistente.Direccion = dto.Direccion;
            usuarioExistente.Estado = dto.Estado;

            await _usuarioRepository.ActualizarAsync(usuarioExistente);
        }
        public async Task ActualizarPerfilAsync(
            int idUsuario, ActualizarPerfilDto dto)
        {
            var usuario = await _usuarioRepository.ObtenerPorIdAsync(idUsuario);
            if(usuario == null)
            {
                throw new Exception("Usuario no encontrado.");
            }
            var correoExistente = await _usuarioRepository.ObtenerPorCorreoAsync(dto.Correo);
            if(correoExistente != null && correoExistente.IdUsuario != idUsuario)
            {
                throw new Exception("El correo ya está registrado por otro usuario.");
            }
            usuario.Nombres = dto.Nombres.Trim();
            usuario.Apellidos = dto.Apellidos.Trim();
            usuario.Correo = dto.Correo.Trim();
            usuario.Telefono = dto.Telefono?.Trim();
            usuario.Direccion = dto.Direccion?.Trim();

            await _usuarioRepository.ActualizarPerfilAsync(usuario);
        }
        public async Task EliminarAsync(int id)
        {
            await _usuarioRepository.EliminarAsync(id);
        }
        public async Task<UsuarioResponseDto?> ObtenerMiPerfilAsync(int idUsuario)
        {
            var usuario = await _usuarioRepository.ObtenerPorIdAsync(idUsuario);
            if (usuario == null)
            {
                return null;
            }
            return UsuarioMapper.toResponseDto(usuario);
        }
    }
}
