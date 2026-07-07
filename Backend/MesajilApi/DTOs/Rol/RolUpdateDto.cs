using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Rol
{
    public class RolUpdateDto
    {
        [Required]
        public int IdRol { get; set; }
        [Required]
        [StringLength(50)]
        public string Nombre { get; set; } = string.Empty;
        [StringLength(150)]
        public string? Descripcion { get; set; }
    }
}
