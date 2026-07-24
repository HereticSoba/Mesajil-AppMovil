using MesajilApi.DTOs.DetalleCarrito;

namespace MesajilApi.Mappings
{
    public static class DetalleCarritoMapper
    {
        public static Models.DetalleCarrito ToEntity(DetalleCarritoCreateDto dto)
        {
            return new Models.DetalleCarrito
            {
                IdCarrito = dto.IdCarrito,
                IdProducto = dto.IdProducto,
                Cantidad = dto.Cantidad,
                PrecioUnitario = dto.PrecioUnitario,
                Subtotal = dto.Cantidad * dto.PrecioUnitario
            };
        }
        public static Models.DetalleCarrito ToEntity(DetalleCarritoUpdateDto dto)
        {
            return new Models.DetalleCarrito
            {
                IdDetalleCarrito = dto.IdDetalleCarrito,
                IdCarrito = dto.IdCarrito,
                IdProducto = dto.IdProducto,
                Cantidad = dto.Cantidad,
                PrecioUnitario = dto.PrecioUnitario,
                Subtotal = dto.Cantidad * dto.PrecioUnitario
            };
        }
        public static DetalleCarritoResponseDto ToResponseDto(Models.DetalleCarrito detalle)
        {
            return new DetalleCarritoResponseDto
            {
                IdDetalleCarrito = detalle.IdDetalleCarrito,
                IdCarrito = detalle.IdCarrito,
                IdProducto = detalle.IdProducto,
                Cantidad = detalle.Cantidad,
                PrecioUnitario = detalle.PrecioUnitario,
                Subtotal = detalle.Subtotal
            };
        }
        public static List<DetalleCarritoResponseDto> ToResponseDtoList(List<Models.DetalleCarrito> detalles)
        {
            return detalles.Select(ToResponseDto).ToList();
        }
    }
}
