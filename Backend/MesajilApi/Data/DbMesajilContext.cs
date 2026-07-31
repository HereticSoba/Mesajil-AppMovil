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
        public DbSet<Carrito> Carritos { get; set; }
        public DbSet<DetalleCarrito> DetalleCarritos { get; set; }
        public DbSet<Pedido> Pedidos { get; set; }
        public DbSet<DetallePedido> DetallePedidos { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Producto>()
                .HasOne(p => p.Inventario)
                .WithOne(i => i.Producto)
                .HasForeignKey<Inventario>(i => i.IdProducto);
        }
    }
}
