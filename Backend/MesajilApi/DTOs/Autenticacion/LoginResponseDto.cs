namespace MesajilApi.DTOs.Autenticador
{
    public class LoginResponseDto
    {
        public string Token { get; set; } = string.Empty;
        public int IdUsuario { get; set; }
        public string Nombres { get; set; } = string.Empty;
        public string Correo {  get; set; } = string.Empty;
        public int IdRol {  get; set; }
    }
}
