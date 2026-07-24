using MesajilApi.DTOs.Pedido;

namespace MesajilApi.Mappings
{
    public static class PedidoMapper
    {
        public static Models.Pedido ToEntity(PedidoCreateDto dto)
        {
            return new Models.Pedido
            {
                IdUsuario = dto.IdUsuario,
                FechaPedido = DateTime.Now,
                Total = dto.Total,
                EstadoPedido = "Pendiente"
            };
        }
        public static Models.Pedido ToEntity(PedidoUpdateDto dto)
        {
            return new Models.Pedido
            {
                IdPedido = dto.IdPedido,
                IdUsuario = dto.IdUsuario,
                Total = dto.Total,
                EstadoPedido = dto.EstadoPedido
            };
        }
        public static PedidoResponseDto ToResponseDto(Models.Pedido pedido)
        {
            return new PedidoResponseDto
            {
                IdPedido = pedido.IdPedido,
                IdUsuario = pedido.IdUsuario,
                FechaPedido = pedido.FechaPedido,
                Total = pedido.Total,
                EstadoPedido = pedido.EstadoPedido
            };
        }
        public static List<PedidoResponseDto> ToResponseDtoList(List<Models.Pedido> pedidos)
        {
            return pedidos.Select(ToResponseDto).ToList();
        }
    }
}
