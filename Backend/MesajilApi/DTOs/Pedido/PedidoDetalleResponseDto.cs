namespace MesajilApi.DTOs.Pedido
{
    public class PedidoDetalleResponseDto
    {
        public int IdPedido { get; set; }
        public DateTime FechaPedido { get; set; }
        public decimal Total { get; set; }
        public string EstadoPedido { get; set; } = string.Empty;
        public string EstadoPago { get; set; } = string.Empty;
        public string TipoEntrega { get; set; } = string.Empty;
        public string? DireccionEntrega { get; set; }
        public string? TiendaRecojo { get; set; }
        public decimal CostoEnvio { get; set; }
        public string? IdOrdenMercadoPago { get; set; }
        public List<DetallePedidoResponseDto> Productos { get; set; } = new ();
    }
}
