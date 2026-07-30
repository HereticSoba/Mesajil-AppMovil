namespace MesajilApi.DTOs.Autenticacion
{
    public class RegistroResponseDto
    {
        public int IdUsuario { get; set; }
        public string Nombres { get; set; } = string.Empty;
        public string Correo { get; set; } = string.Empty;
        public string Mensaje { get; set; } = string.Empty;
    }
}
