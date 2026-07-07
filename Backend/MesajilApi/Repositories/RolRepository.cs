using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class RolRepository : IRolRepository
    {
        private readonly DbMesajilContext _context;
        public RolRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<Rol>> ObtenerTodosAsync()
        {
            return await _context.Roles.ToListAsync();
        }
        public async Task<Rol?> ObtenerPorIdAsync(int id)
        {
            return await _context.Roles.FindAsync(id);
        }
        public async Task<Rol> CrearAsync(Rol rol)
        {
            _context.Roles.Add(rol);
            await _context.SaveChangesAsync();
            return rol;
        }
        public async Task ActualizarAsync(Rol rol)
        {
            _context.Roles.Update(rol);
            await _context.SaveChangesAsync();
        }
        public async Task EliminarAsync(int id)
        {
            var rol = await _context.Roles.FindAsync(id);
            if (rol != null)
            {
                _context.Roles.Remove(rol);
                await _context.SaveChangesAsync();
            }
        }
    }
}
