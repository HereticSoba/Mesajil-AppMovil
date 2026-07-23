using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Data
{
    public class DbMesajilContext : DbContext
    {
        public DbMesajilContext(DbContextOptions<DbMesajilContext>options) :base(options){}
        public DbSet<Rol> Roles { get; set; }
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Categoria> Categorias { get; set; }
        public DbSet<Producto> Productos { get; set; }
        public DbSet<ImagenProducto> ImagenesProductos { get; set; }
        public DbSet<Inventario> Inventarios { get; set; }
    }
}
