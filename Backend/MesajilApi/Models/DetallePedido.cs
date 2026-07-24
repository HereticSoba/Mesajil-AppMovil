using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("DetallePedido")]
    public class DetallePedido
    {
        [Key]
        public int IdDetallePedido { get; set; }
        public int IdPedido { get; set; }
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }
        
        [Column(TypeName = "decimal(10,2)")]
        public decimal PrecioUnitario { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Subtotal { get; set; }

        [ForeignKey(nameof(IdPedido))]
        public Pedido? Pedido { get; set; }

        [ForeignKey(nameof(IdProducto))]
        public Producto? Producto { get; set; }
    }
}
