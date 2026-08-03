namespace MesajilApi.DTOs.Pago
{
    public class PagoRequestDto
    {
        public decimal Monto { get; set; }
        public string Email { get; set; } = string.Empty;
        public string TokenTarjeta { get; set; } = string.Empty;
        public string MetodoPago { get; set; } = string.Empty;
        public string TipoMetodoPago { get; set; } = string.Empty;
        public int Cuotas { get; set; } = 1;
    }
}
