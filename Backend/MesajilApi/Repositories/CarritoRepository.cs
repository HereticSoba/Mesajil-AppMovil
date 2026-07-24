using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class CarritoRepository : ICarritoRepository
    {
        private readonly DbMesajilContext _context;
        public CarritoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<List<Carrito>> ObtenerTodosAsync()
        {
            return await _context.Carritos
                .Include(c => c.Usuario)
                .ToListAsync();
        }
        public async Task<Carrito?> ObtenerPorIdAsync(int id)
        {
            return await _context.Carritos
                .Include (c => c.Usuario)
                .FirstOrDefaultAsync(c => c.IdCarrito == id);
        }
        public async Task<Carrito> CrearAsync(Carrito carrito)
        {
            _context.Carritos.Add(carrito);
            await _context.SaveChangesAsync();
            return carrito;
        }
        public async Task<Carrito> ActualizarAsync(Carrito carrito)
        {
            _context.Carritos.Update(carrito);
            await _context.SaveChangesAsync();
            return carrito;
        }
        public async Task<bool> EliminarAsync(int id)
        {
            var carrito = await _context.Carritos.FindAsync(id);
            if(carrito == null)
                return false;
            _context.Carritos.Remove(carrito);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
