using MesajilApi.Models;
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
        public async Task<IEnumerable<Rol>> ObtenerTodosAsync()
        {
            return await _rolRepository.ObtenerTodosAsync();
        }
        public async Task<Rol?> ObtenerPorIdAsync(int id)
        {
            return await _rolRepository.ObtenerPorIdAsync(id);
        }
        public async Task<Rol> CrearAsync(Rol rol)
        {
            return await _rolRepository.CrearAsync(rol);
        }
        public async Task ActualizarAsync(Rol rol)
        {
            await _rolRepository.ActualizarAsync(rol);
        }
        public async Task EliminarAsync(int id)
        {
            await _rolRepository.EliminarAsync(id);
        }
    }
}
