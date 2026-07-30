using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Usuario
{
    public class UsuarioRegistroDto
    {
        [Required]
        public string Nombres { get; set; } = string.Empty;

        [Required]
        public string Apellidos { get; set; } = string.Empty;

        [Required]
        [EmailAddress]
        public string Correo { get; set; } = string.Empty;

        [Required]
        public string Contrasena { get; set; } = string.Empty;

        public string? Telefono { get; set; }

        public string? Direccion { get; set; }
    }
}
