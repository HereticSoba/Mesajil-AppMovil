using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Carrito")]
    public class Carrito
    {
        [Key]
        public int IdCarrito { get; set; }
        public int IdUsuario { get; set; }
        public DateTime FechaCreacion { get; set; }
        public bool Estado {  get; set; }
        [ForeignKey(nameof(IdUsuario))]
        public Usuario? Usuario { get; set; }
    }
}
