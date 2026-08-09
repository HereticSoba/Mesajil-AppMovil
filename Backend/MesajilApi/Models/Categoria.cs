using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("categoria")]
    public class Categoria
    {
        [Key]
        public int IdCategoria { get; set; }
        [Required]
        [StringLength(100)]
        public string Nombre { get; set; } = string.Empty;
        [StringLength(300)]
        public string? Descripcion { get; set; }
        [Required]
        public DateTime FechaRegistro { get; set; }
        [Required]
        public bool Estado {  get; set; }
    }
}
