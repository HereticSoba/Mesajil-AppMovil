using MesajilApi.DTOs.Usuario;
using System.Collections;
using UsuarioEntity = MesajilApi.Models.Usuario;

namespace MesajilApi.Mappings
{
    public static class UsuarioMapper
    {
        public static UsuarioEntity toEntity(UsuarioCreateDto dto)
        {
            return new UsuarioEntity
            {
                IdRol = dto.IdRol,
                Nombres = dto.Nombres,
                Apellidos = dto.Apellidos,
                Correo = dto.Correo,
                Contrasena = dto.Contrasena,
                Telefono = dto.Telefono,
                Direccion = dto.Direccion,
                FechaRegistro = DateTime.Now,
                Estado = true
            };
        }

        public static UsuarioEntity toEntity(UsuarioUpdateDto dto)
        {
            return new UsuarioEntity
            {
                IdUsuario = dto.IdUsuario,
                IdRol = dto.IdRol,
                Nombres = dto.Nombres,
                Apellidos = dto.Apellidos,
                Correo = dto.Correo,
                Telefono = dto.Telefono,
                Direccion = dto.Direccion,
                Estado = dto.Estado
            };
        }

        public static UsuarioResponseDto toResponseDto(UsuarioEntity usuario)
        {
            return new UsuarioResponseDto
            {
                IdUsuario = usuario.IdUsuario,
                IdRol = usuario.IdRol,
                Nombres = usuario.Nombres,
                Apellidos = usuario.Apellidos,
                Correo = usuario.Correo,
                Telefono = usuario.Telefono,
                Direccion = usuario.Direccion,
                FechaRegistro = usuario.FechaRegistro,
                Estado = usuario.Estado
            };
        }

        public static List<UsuarioResponseDto> toResponseDtoList(IEnumerable<UsuarioEntity> usuarios)
        {
            return usuarios.Select(toResponseDto).ToList();
        }
    }
}
