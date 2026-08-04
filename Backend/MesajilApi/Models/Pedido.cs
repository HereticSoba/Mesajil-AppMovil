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
        [StringLength(20)]
        public string TipoEntrega { get; set; } = string.Empty;
        [StringLength(200)]
        public string? DireccionEntrega { get; set; }
        [StringLength(100)]
        public string? TiendaRecojo { get; set; }
        [Column(TypeName = "decimal(10,2)")]
        public decimal CostoEnvio { get; set; }
        [StringLength(30)]
        public string EstadoPago { get; set; } = string.Empty;
        [StringLength(100)]
        public string? IdOrdenMercadoPago { get; set; }
        [ForeignKey(nameof(IdUsuario))]
        public Usuario? Usuario { get; set; }
    }
}
