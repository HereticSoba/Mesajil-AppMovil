using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class DetalleCarritoRepository : IDetalleCarritoRepository
    {
        private readonly DbMesajilContext _context;
        public DetalleCarritoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<List<DetalleCarrito>> ObtenerTodosAsync()
        {
            return await _context.DetalleCarritos
                .Include(d => d.Carrito)
                .Include(d => d.Producto)
                .ToListAsync();
        }
        public async Task<DetalleCarrito?> ObtenerPorIdAsync(int id)
        {
            return await _context.DetalleCarritos
                .Include(d => d.Carrito)
                .Include(d => d.Producto)
                .FirstOrDefaultAsync(d => d.IdDetalleCarrito == id);
        }
        public async Task<DetalleCarrito> CrearAsync(DetalleCarrito detalle)
        {
            _context.DetalleCarritos.Add(detalle);
            await _context.SaveChangesAsync();
            return detalle;
        }
        public async Task<DetalleCarrito> ActualizarAsync(DetalleCarrito detalle)
        {
            _context.DetalleCarritos.Update(detalle);
            await _context.SaveChangesAsync();
            return detalle;
        }
        public async Task<bool> EliminarAsync(int id)
        {
            var detalle = await _context.DetalleCarritos.FindAsync(id);
            if(detalle == null)
                return false;
            _context.DetalleCarritos.Remove(detalle);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}