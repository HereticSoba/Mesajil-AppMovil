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
            var entidad = UsuarioMapper.toEntity(dto);
            await _usuarioRepository.ActualizarAsync(entidad);
        }
        public async Task EliminarAsync(int id)
        {
            await _usuarioRepository.EliminarAsync(id);
        }
    }
}
