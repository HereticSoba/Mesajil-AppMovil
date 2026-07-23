using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Inventario")]
    public class Inventario
    {
        [Key]
        public int IdInventario { get; set; }
        public int IdProducto { get; set; }
        public int StockActual { get; set; }
        public int StockMinimo { get; set; }
        public DateTime UltimaActualizacion {  get; set; }
        [ForeignKey(nameof(IdProducto))]
        public Producto? Producto { get; set; }
    }
}
