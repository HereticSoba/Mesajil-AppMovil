using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class InventarioRepository : IInventarioRepository
    {
        private readonly DbMesajilContext _context;
        public InventarioRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<Inventario>> ObtenerTodosAsync()
        {
            return await _context.Inventarios
                .Include(i => i.Producto)
                .ToListAsync();
        }
        public async Task<Inventario?> ObtenerPorIdAsync(int id)
        {
            return await _context.Inventarios
                .Include(i => i.Producto)
                .FirstOrDefaultAsync(i => i.IdInventario == id);
        }
        public async Task CrearAsync(Inventario inventario)
        {
            _context.Inventarios.Add(inventario);
            await _context.SaveChangesAsync();
        }
        public async Task ActualizarAsync(Inventario inventario)
        {
            _context.Inventarios.Update(inventario);
            await _context.SaveChangesAsync();
        }
        public async Task EliminarAsync(int id)
        {
            var inventario = await _context.Inventarios.FindAsync(id);
            if(inventario != null)
            {
                _context.Inventarios.Remove(inventario);
                await _context.SaveChangesAsync();
            }
        }
    }
}
