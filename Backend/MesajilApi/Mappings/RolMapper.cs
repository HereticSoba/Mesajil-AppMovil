using MesajilApi.DTOs.Rol;
using RolEntity = MesajilApi.Models.Rol;

namespace MesajilApi.Mappings
{
    public static class RolMapper
    {
        public static RolEntity ToEntity(RolCreateDto dto)
        {
            return new RolEntity
            {
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion,
            };
        }

        public static RolEntity ToEntity(RolUpdateDto dto)
        {
            return new RolEntity
            {
                IdRol = dto.IdRol,
                Nombre = dto.Nombre,
                Descripcion = dto.Descripcion
            };
        }
        public static RolResponseDto ToResponseDto(RolEntity rol)
        {
            return new RolResponseDto
            {
                IdRol = rol.IdRol,
                Nombre = rol.Nombre,
                Descripcion = rol.Descripcion
            };
        }
        public static List<RolResponseDto> ToResponseDtoList(IEnumerable<RolEntity> roles)
        {
            return roles.Select(ToResponseDto).ToList();
        }
    }
}
