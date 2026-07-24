using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("DetalleCarrito")]
    public class DetalleCarrito
    {
        [Key]
        public int IdDetalleCarrito { get; set; }
        public int IdCarrito { get; set; }
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal PrecioUnitario { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Subtotal { get; set; }

        [ForeignKey(nameof(IdCarrito))]
        public Carrito? Carrito { get; set; }

        [ForeignKey(nameof(IdProducto))]
        public Producto? Producto { get; set; }
    }
}
