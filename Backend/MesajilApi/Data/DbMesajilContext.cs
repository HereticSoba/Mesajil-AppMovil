using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Data
{
    public class DbMesajilContext : DbContext
    {
        public DbMesajilContext(DbContextOptions<DbMesajilContext>options) :base(options){}
        public DbSet<Rol> Roles { get; set; }
    }
}
