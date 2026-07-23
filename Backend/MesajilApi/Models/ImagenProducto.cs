using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("ImagenesProducto")]
    public class ImagenProducto
    {
        [Key]
        public int IdImagen { get; set; }
        public int IdProducto { get; set; }
        [Required]
        [StringLength(255)]
        public string UrlImagen { get; set; } = string.Empty;
        public bool Principal {  get; set; }
        [ForeignKey(nameof(IdProducto))]
        public Producto? Producto { get; set; }
    }
}
