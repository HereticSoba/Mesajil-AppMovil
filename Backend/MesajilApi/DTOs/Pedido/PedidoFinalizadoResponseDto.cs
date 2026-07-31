namespace MesajilApi.DTOs.Pedido
{
    public class PedidoFinalizadoResponseDto
    {
        public int IdPedido { get; set; }
        public decimal Total { get; set; }
        public string Mensaje { get; set; } = string.Empty;
    }
}
