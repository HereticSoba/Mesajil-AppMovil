using MesajilApi.Data;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Repositories
{
    public class CategoriaRepository : ICategoriaRepository
    {
        private readonly DbMesajilContext _context;
        public CategoriaRepository(DbMesajilContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<Categoria>> ObtenerTodosAsync()
        {
            return await _context.Categorias.ToListAsync();
        }
        public async Task<Categoria?> ObtenerPorIdAsync(int id)
        {
            return await _context.Categorias.FindAsync(id);
        }
        public async Task<Categoria> CrearAsync(Categoria categoria)
        {
            _context.Categorias.Add(categoria);
            await _context.SaveChangesAsync();
            return categoria;
        }
        public async Task ActualizarAsync(Categoria categoria)
        {
            _context.Categorias.Update(categoria);
            await _context.SaveChangesAsync();
        }
        public async Task EliminarAsync(int id)
        {
            var categoria = await _context.Categorias.FindAsync(id);
            if (categoria == null)
            {
                _context.Categorias.Remove(categoria);
                await _context.SaveChangesAsync();
            }
        }
    }
}
