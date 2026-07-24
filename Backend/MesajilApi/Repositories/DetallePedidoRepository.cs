using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class DetallePedidoRepository : IDetallePedidoRepository
    {
        private  readonly DbMesajilContext _context;
        public DetallePedidoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<List<DetallePedido>> ObtenerTodosAsync()
        {
            return await _context.DetallePedidos
                .Include(d => d.Pedido)
                .Include(d => d.Producto)
                .ToListAsync();
        }
        public async Task<DetallePedido?> ObtenerPorIdAsync(int id)
        {
            return await _context.DetallePedidos
                .Include(d => d.Pedido)
                .Include(d => d.Producto)
                .FirstOrDefaultAsync(d => d.IdDetallePedido == id);
        }
        public async Task<DetallePedido> CrearAsync(DetallePedido detallePedido)
        {
            _context.DetallePedidos.Add(detallePedido);
            await _context.SaveChangesAsync();
            return detallePedido;
        }
        public async Task<DetallePedido> ActualizarAsync(DetallePedido detallePedido)
        {
            _context.DetallePedidos.Update(detallePedido);
            await _context.SaveChangesAsync();
            return detallePedido;
        }
        public async Task<bool> EliminarAsync(int id)
        {
            var detalle = await _context.DetallePedidos.FindAsync(id);
            if (detalle == null)
                return false;
            _context.DetallePedidos.Remove(detalle);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
