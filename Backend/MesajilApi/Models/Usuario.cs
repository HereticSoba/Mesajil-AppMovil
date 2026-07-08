using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MesajilApi.Models
{
    [Table("Usuario")]
    public class Usuario
    {
        [Key]
        public int IdUsuario { get; set; }

        [Required]
        public int IdRol {  get; set; }

        [Required]
        [StringLength(100)]
        public string Nombres { get; set; } = string.Empty;

        [Required]
        [StringLength(100)]
        public string Apellidos { get; set; } = string.Empty;

        [Required]
        [StringLength(100)]
        public string Correo { get; set; } = string.Empty;

        [Required]
        [StringLength(255)]
        public string Contraseña { get; set; } = string.Empty;

        [StringLength(20)]
        public string? Telefono {  get; set; }

        [StringLength(200)]
        public string? Direccion { get; set; }

        [Required]
        public DateTime FechaRegistro { get; set; }

        [Required]
        public bool Estado {  get; set; }

        [ForeignKey(nameof(IdRol))]
        public Rol? Rol { get; set; }
    }
}
