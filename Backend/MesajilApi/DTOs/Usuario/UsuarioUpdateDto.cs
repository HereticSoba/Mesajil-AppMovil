using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Usuario
{
    public class UsuarioUpdateDto
    {
        [Required]
        public int IdUsuario {  get; set; }

        [Required]
        public int IdRol {  get; set; }

        [Required]
        [StringLength(100)]
        public string Nombres { get; set; } = string.Empty;

        [Required]
        [StringLength(100)]
        public string Apellidos { get; set; } = string.Empty;

        [Required]
        [EmailAddress]
        public string Correo { get; set; } = string.Empty;

        [StringLength(20)]
        public string? Telefono { get; set; }

        [StringLength(200)]
        public string? Direccion { get; set; }

        public bool Estado { get; set; }
    }
}
