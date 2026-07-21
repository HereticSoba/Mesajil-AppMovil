using MesajilApi.DTOs.Categoria;
using CategoriaEntity = MesajilApi.Models.Categoria;

namespace MesajilApi.Mappings
{
    public static class CategoriaMapper
    {
        public static CategoriaEntity toEntity(CategoriaCreateDto dto)
        {
            return new CategoriaEntity
            {
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion
            };
        }
        public static CategoriaEntity toEntity(CategoriaUpdateDto dto)
        {
            return new CategoriaEntity
            {
                IdCategoria = dto.IdCategoria,
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion
            };
        }
        public static CategoriaResponseDto toResponseDto(CategoriaEntity categoria)
        {
            return new CategoriaResponseDto
            {
                IdCategoria = categoria.IdCategoria,
                Nombre = categoria.Nombre,
                Descripcion = categoria.Descripcion,
                FechaRegistro = categoria.FechaRegistro,
                Estado = categoria.Estado
            };
        }
        public static List<CategoriaResponseDto> toResponseDtoList(IEnumerable<CategoriaEntity> categorias)
        {
            return categorias.Select(toResponseDto).ToList();
        }
    }
}
