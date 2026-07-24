namespace MesajilApi.DTOs.Pedido
{
    public class PedidoResponseDto
    {
        public int IdPedido { get; set; }
        public int IdUsuario { get; set; }
        public DateTime FechaPedido { get; set; }
        public decimal Total {  get; set; }
        public string EstadoPedido { get; set; }  = string.Empty;
    }
}
