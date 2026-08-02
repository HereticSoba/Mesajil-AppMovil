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
                .ThenInclude(p => p!.Inventario)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Imagenes)
                .ToListAsync();
        }
        public async Task<DetalleCarrito?> ObtenerPorIdAsync(int id)
        {
            return await _context.DetalleCarritos
                .Include(d => d.Carrito)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Inventario)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Imagenes)
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
        public async Task<List<DetalleCarrito>> ObtenerPorCarritoAsync(int idCarrito)
        {
            return await _context.DetalleCarritos
                .Include(d => d.Carrito)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Inventario)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Imagenes)
                .Where(d => d.IdCarrito == idCarrito)
                .ToListAsync();
        }
        public async Task<DetalleCarrito?> ObtenerPorCarritoYProductoAsync(int idCarrito, int idProducto)
        {
            return await _context.DetalleCarritos
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Inventario)
                .Include(d => d.Producto)
                .ThenInclude(p => p!.Imagenes)
                .FirstOrDefaultAsync(d => d.IdCarrito == idCarrito && d.IdProducto == idProducto);
        }
        public async Task EliminarPorCarritoAsync(int idCarrito)
        {
            var detalles = await _context.DetalleCarritos
                .Where(d => d.IdCarrito == idCarrito)
                .ToListAsync();
            _context.DetalleCarritos.RemoveRange(detalles);
            await _context.SaveChangesAsync();
        }
    }
}