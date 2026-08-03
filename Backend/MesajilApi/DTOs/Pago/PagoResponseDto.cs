namespace MesajilApi.DTOs.Pago
{
    public class PagoResponseDto
    {
        public string IdOrden { get; set; } = string.Empty;
        public string Estado { get; set; } = string.Empty;
        public string? DetalleEstado { get; set; }
        public decimal Monto { get; set; }
        public string Mensaje { get; set; } = string.Empty;
    }
}
