using MesajilApi.DTOs.Inventario;
using MesajilApi.Models;

namespace MesajilApi.Mappings
{
    public static class InventarioMapper
    {
        public static Inventario ToEntity(InventarioCreateDto dto)
        {
            return new Inventario
            {
                IdProducto = dto.IdProducto,
                StockActual = dto.StockActual,
                StockMinimo = dto.StockMinimo,
                UltimaActualizacion = DateTime.Now
            };
        }
        public static Inventario ToEntity(InventarioUpdateDto dto)
        {
            return new Inventario
            {
                IdInventario = dto.IdInventario,
                IdProducto = dto.IdProducto,
                StockActual = dto.StockActual,
                StockMinimo = dto.StockMinimo,
                UltimaActualizacion = DateTime.Now
            };
        }
        public static InventarioResponseDto ToResponseDto(Inventario inventario)
        {
            return new InventarioResponseDto
            {
                IdInventario = inventario.IdInventario,
                IdProducto = inventario.IdProducto,
                StockActual = inventario.StockActual,
                StockMinimo = inventario.StockMinimo,
                UlitmaActualizacion = inventario.UltimaActualizacion
            };
        }
        public static IEnumerable<InventarioResponseDto> ToResponseDtolist(IEnumerable<Inventario> inventarios)
        {
            return inventarios.Select(i => ToResponseDto(i));
        }
    }
}
