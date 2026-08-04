namespace MesajilApi.DTOs.Pago
{
    public class PagoRequestDto
    {
        public string Email { get; set; } = string.Empty;
        public string Token { get; set; } = string.Empty;
        public string MetodoPago { get; set; } = string.Empty;
        public string TipoMetodoPago { get; set; } = string.Empty;
        public int Cuotas { get; set; } = 1;
        public string TipoDocumento { get; set; } = string.Empty;
        public string NumeroDocumento { get; set; } = string.Empty;
        public string TipoEntrega { get; set; } = string.Empty;
        public string? DireccionEntrega { get; set; }
    }
}
