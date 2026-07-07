using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Rol")]
    public class Rol
    {
        [Key]
        public int IdRol {  get; set; }

        [Required]
        [StringLength(50)]
        public string Nombre { get; set; } = string.Empty;

        [StringLength(150)]
        public string? Descripcion { get; set; }
    }
}
