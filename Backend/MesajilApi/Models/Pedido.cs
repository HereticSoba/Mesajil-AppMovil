using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Pedido")]
    public class Pedido
    {
        [Key]
        public int IdPedido {  get; set; }
        public int IdUsuario { get; set; }
        public DateTime FechaPedido { get; set; }
        [Column(TypeName = "decimal(10,2)")]
        public decimal Total {  get; set; }
        [StringLength(30)]
        public string EstadoPedido { get; set; } = string.Empty;
        [ForeignKey(nameof(IdUsuario))]
        public Usuario? Usuario { get; set; }
    }
}
