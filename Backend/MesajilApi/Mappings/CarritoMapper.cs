using MesajilApi.DTOs.Carrito;

namespace MesajilApi.Mappings
{
    public static class CarritoMapper
    {
        public static Models.Carrito toEntity(CarritoCreateDto dto)
        {
            return new Models.Carrito
            {
                IdUsuario = dto.IdUsuario,
                FechaCreacion = DateTime.Now,
                Estado = true
            };
        }
        public static Models.Carrito ToEntity(CarritoUpdateDto dto)
        {
            return new Models.Carrito
            {
                IdCarrito = dto.IdCarrito,
                IdUsuario = dto.IdUsuario,
                Estado = true
            };
        }
        public static CarritoResponseDto ToResponseDto(Models.Carrito carrito)
        {
            return new CarritoResponseDto
            {
                IdCarrito = carrito.IdCarrito,
                IdUsuario = carrito.IdUsuario,
                FechaCreacion = carrito.FechaCreacion,
                Estado = carrito.Estado
            };
        }
        public static List<CarritoResponseDto> ToResponseDtoList(List<Models.Carrito> carritos)
        {
            return carritos.Select(ToResponseDto).ToList();
        }
    }
}
