using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Usuario
{
    public class UsuarioLoginDto
    {
        [Required]
        [EmailAddress]
        public string Correo { get; set; } = string.Empty;

        [Required]
        public string Contrasena { get; set; } = string.Empty;
    }
}
