namespace MesajilApi.DTOs.Usuario
{
    public class UsuarioResponseDto
    {
        public int IdUsuario { get; set; }
        public int IdRol {  get; set; }
        public string Nombres { get; set; } = string.Empty;
        public string Apellidos { get; set; } = string.Empty;
        public string Correo {  get; set; } = string.Empty;
        public string? Telefono {  get; set; }
        public string? Direccion { get; set; }
        public DateTime FechaRegistro { get; set; }
        public bool Estado { get; set; }
    }
}
