using MesajilApi.DTOs.Producto;
using MesajilApi.Models;

namespace MesajilApi.Mappings
{
    public static class ProductoMapper
    {
        public static Producto ToEntity(ProductoCreateDto dto)
        {
            return new Producto
            {
                IdCategoria = dto.IdCategoria,
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion,
                Marca = dto.Marca,
                Modelo = dto.Modelo,
                Precio = dto.Precio,
                FechaRegistro = DateTime.Now,
                Estado = true
            };
        }
        public static Producto ToEntity(ProductoUpdateDto dto)
        {
            return new Producto
            {
                IdProducto = dto.IdProducto,
                IdCategoria = dto.IdCategoria,
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion,
                Marca = dto.Marca,
                Modelo = dto.Modelo,
                Precio = dto.Precio,
            };
        }
        public static ProductoResponseDto ToResponseDto(Producto producto)
        {
            return new ProductoResponseDto
            {
                IdProducto = producto.IdProducto,
                IdCategoria = producto.IdCategoria,
                Nombre = producto.Nombre,
                Descripcion = producto.Descripcion,
                Marca = producto.Marca,
                Modelo = producto.Modelo,
                Precio = producto.Precio,
                Estado = producto.Estado,
                FechaRegistro = producto.FechaRegistro,
            };
        }
        public static List<ProductoResponseDto> ToResponseDtoList(IEnumerable<Producto> productos)
        {
            return productos.Select(ToResponseDto).ToList();
        }
    }
}
