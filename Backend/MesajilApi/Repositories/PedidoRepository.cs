using MesajilApi.Data;
using MesajilApi.DTOs.Pedido;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class PedidoRepository : IPedidoRepository
    {
        private readonly DbMesajilContext _context;
        public PedidoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<List<Pedido>> ObtenerTodosAsync()
        {
            return await _context.Pedidos
                .Include(p => p.Usuario)
                .ToListAsync();
        }
        public async Task<Pedido?> ObtenerPorIdAsync(int id)
        {
            return await _context.Pedidos
                .Include (p => p.Usuario)
                .FirstOrDefaultAsync(p => p.IdPedido == id);
        }
        public async Task<Pedido> CrearAsync(Pedido pedido)
        {
            _context.Pedidos.Add(pedido);
            await _context.SaveChangesAsync();
            return pedido;
        }
        public async Task<Pedido> ActualizarAsync(Pedido pedido)
        {
            _context.Pedidos.Update(pedido);
            await _context.SaveChangesAsync();
            return pedido;
        }
        public async Task<bool> EliminarAsync(int id)
        {
            var pedido = await _context.Pedidos.FindAsync(id);
            if(pedido == null)
                return false;
            _context.Pedidos.Remove(pedido);
            await _context.SaveChangesAsync();
            return true;
        }
        public async Task<List<Pedido>> ObtenerPorUsuarioAsync(int idUsuario)
        {
            return await _context.Pedidos
                .Where(p => p.IdUsuario == idUsuario)
                .OrderByDescending(p => p.FechaPedido)
                .ToListAsync();
        }
        public async Task<List<ProductoMayorDemandaResponseDto>>ObtenerProductosMayorDemandaAsync()
        {
            return await _context.DetallePedidos
                .Where(d =>d.Pedido != null && d.Pedido.EstadoPedido != "Cancelado")
                .GroupBy(d => new{
                    d.IdProducto, Nombre = d.Producto!.Nombre
                })
                .Select(g => new ProductoMayorDemandaResponseDto
                {
                    IdProducto = g.Key.IdProducto,
                    Nombre = g.Key.Nombre,
                    CantidadVendida = g.Sum(d => d.Cantidad)
                })
                .OrderByDescending(x => x.CantidadVendida)
                .ToListAsync();
        }
    }
}
