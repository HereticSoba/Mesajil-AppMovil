namespace MesajilApi.DTOs.Pedido
{
    public class PedidoUpdateDto
    {
        public int IdPedido { get; set; }
        public int IdUsuario { get; set; }
        public decimal Total {  get; set; }
        public string EstadoPedido { get; set; } = string.Empty;
    }
}
