using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class ProductoRepository : IProductoRepository
    {
        private readonly DbMesajilContext _context;
        public ProductoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<Producto>> ObtenerTodosAsync()
        {
            return await _context.Productos
                .Include(p => p.Categoria)
                .ToListAsync();
        }
        public async Task<Producto?> ObtenerPorIdAsync(int id)
        {
            return await _context.Productos
                .Include (p => p.Categoria)
                .FirstOrDefaultAsync(P => P.IdProducto == id);
        }
        public async Task<Producto> CrearAsync(Producto producto)
        {
            _context.Productos.Add(producto);
            await _context.SaveChangesAsync();
            return producto;
        }
        public async Task ActualizarAsync(Producto producto)
        {
            _context.Productos.Update(producto);
            await _context.SaveChangesAsync();
        }
        public async Task EliminarAsync(int id)
        {
            var producto = await _context.Productos.FindAsync(id);
            if(producto != null)
            {
                _context.Productos.Remove(producto);
                await _context.SaveChangesAsync();
            }
        }
    }
}
