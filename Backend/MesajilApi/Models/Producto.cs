using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Producto")]
    public class Producto
    {
        [Key]
        public int IdProducto { get; set; }
        [Required]
        public int IdCategoria { get; set; }
        [Required]
        [StringLength(120)]
        public string Nombre { get; set; } = string.Empty;
        public string? Descripcion { get; set; }
        [Required]
        [StringLength(80)]
        public string Marca { get; set; } = string.Empty;
        [Required]
        [StringLength(80)]
        public string Modelo {  get; set; } = string.Empty;
        [Required]
        [Column(TypeName = "decimal(10,2)")]
        public decimal Precio { get; set; }
        [Required]
        public bool Estado { get; set; }
        [Required]
        public DateTime FechaRegistro { get; set; }
        [ForeignKey(nameof(IdCategoria))]
        public Categoria? Categoria { get; set; }
        public Inventario? Inventario { get; set; }
        public ICollection<ImagenProducto> Imagenes { get; set; }
            = new List<ImagenProducto>();
        }
}
