using MesajilApi.DTOs.Rol;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class RolService : IRolService
    {
        private readonly IRolRepository _rolRepository;
        public RolService(IRolRepository rolRepository)
        {
            _rolRepository = rolRepository;
        }
        public async Task<IEnumerable<RolResponseDto>> ObtenerTodosAsync()
        {
            var roles = await _rolRepository.ObtenerTodosAsync();
            return RolMapper.ToResponseDtoList(roles);
        }
        public async Task<RolResponseDto?> ObtenerPorIdAsync(int id)
        {
            var rol = await _rolRepository.ObtenerPorIdAsync(id);
            if (rol == null)
                return null;
            
            return RolMapper.ToResponseDto(rol);
        }
        public async Task<RolResponseDto> CrearAsync(RolCreateDto dto)
        {
            var entidad = RolMapper.ToEntity(dto);
            var nuevoRol = await _rolRepository.CrearAsync(entidad);
            return RolMapper.ToResponseDto(nuevoRol);
        }
        public async Task ActualizarAsync(RolUpdateDto dto)
        {
            var entidad = RolMapper.ToEntity(dto);
            await _rolRepository.ActualizarAsync(entidad);
        }
        public async Task EliminarAsync(int id)
        {
            await _rolRepository.EliminarAsync(id);
        }
    }
}
