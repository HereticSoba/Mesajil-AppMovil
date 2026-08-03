namespace MesajilApi.DTOs.Pedido
{
    public class FinalizarCompraDto
    {
        public string TipoEntrega { get; set; } = string.Empty;
        public string? DireccionEntrega { get; set; }
    }
}
