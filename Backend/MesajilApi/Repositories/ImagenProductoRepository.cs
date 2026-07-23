using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class ImagenProductoRepository : IImagenProductoRepository
    {
        private readonly DbMesajilContext _context;
        public ImagenProductoRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<ImagenProducto>> ObtenerTodosAsync()
        {
            return await _context.ImagenesProductos
                .Include(i => i.Producto)
                .ToListAsync();
        }
        public async Task<ImagenProducto?> ObtenerPorIdAsync(int id)
        {
            return await _context.ImagenesProductos
                .Include(i => i.Producto)
                .FirstOrDefaultAsync(i => i.IdImagen == id);
        }
        public async Task<ImagenProducto> CrearAsync(ImagenProducto imagenProducto)
        {
            _context.ImagenesProductos.Add(imagenProducto);
            await _context.SaveChangesAsync();
            return imagenProducto;
        }
        public async Task ActualizarAsync(ImagenProducto imagenProducto)
        {
            _context.ImagenesProductos.Update(imagenProducto);
            await _context.SaveChangesAsync();
        }
        public async Task EliminarAsync(int id)
        {
            var imagen = await _context.ImagenesProductos.FindAsync(id);
            if (imagen != null)
            {
                _context.ImagenesProductos.Remove(imagen);
                await _context.SaveChangesAsync();
            }
        }
    }
}
