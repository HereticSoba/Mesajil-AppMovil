using MesajilApi.DTOs.DetallePedido;
using MesajilApi.Models;

namespace MesajilApi.Mappings
{
    public static class DetallePedidoMapper
    {
        public static DetallePedido ToEntity(DetallePedidoCreateDto dto)
        {
            return new DetallePedido
            {
                IdPedido = dto.IdPedido,
                IdProducto = dto.IdProducto,
                Cantidad = dto.Cantidad,
                PrecioUnitario = dto.PrecioUnitario,
                Subtotal = dto.Cantidad * dto.PrecioUnitario
            };
        }
        public static DetallePedido ToEntity(DetallePedidoUpdateDto dto)
        {
            return new DetallePedido
            {
                IdDetallePedido = dto.IdDetallePedido,
                IdPedido = dto.IdPedido,
                IdProducto = dto.IdProducto,
                Cantidad = dto.Cantidad,
                PrecioUnitario = dto.PrecioUnitario,
                Subtotal = dto.Cantidad * dto.PrecioUnitario
            };
        }
        public static DetallePedidoResponseDto ToResponseDto(DetallePedido detalle)
        {
            return new DetallePedidoResponseDto
            {
                IdDetallePedido = detalle.IdDetallePedido,
                IdPedido = detalle.IdPedido,
                IdProducto = detalle.IdProducto,
                Cantidad = detalle.Cantidad,
                PrecioUnitario = detalle.PrecioUnitario,
                Subtotal = detalle.Subtotal,
            };
        }
        public static List<DetallePedidoResponseDto> ToResponseDtoList(List<DetallePedido> detalles)
        {
            return detalles.Select(ToResponseDto).ToList();
        }
    }
}
