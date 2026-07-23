using MesajilApi.DTOs.ImagenProducto;
using MesajilApi.Models;
using Microsoft.AspNetCore.Authentication;

namespace MesajilApi.Mappings
{
    public static class ImagenProductoMapper
    {
        public static ImagenProducto ToEntity(ImagenProductoCreateDto dto)
        {
            return new ImagenProducto
            {
                IdProducto = dto.IdProducto,
                Principal = dto.Principal
            };
        }
        public static ImagenProducto ToEntity(ImagenProductoUpdateDto dto)
        {
            return new ImagenProducto
            {
                IdImagen = dto.IdImagen,
                IdProducto = dto.IdProducto,
                Principal = dto.Principal
            };
        }
        public static ImagenProductoResponseDto ToResponseDto(ImagenProducto imagen)
        {
            return new ImagenProductoResponseDto
            {
                IdImagen = imagen.IdImagen,
                IdProducto = imagen.IdProducto,
                UrlImagen = imagen.UrlImagen,
                Principal = imagen.Principal
            };
        }
        public static List<ImagenProductoResponseDto> ToResponseDtoList(IEnumerable<ImagenProducto> imagenes)
        {
            return imagenes.Select(ToResponseDto).ToList();
        }
    }
}
