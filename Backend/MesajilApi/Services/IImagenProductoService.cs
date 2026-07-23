using MesajilApi.DTOs.ImagenProducto;

namespace MesajilApi.Services
{
    public interface IImagenProductoService
    {
        Task<IEnumerable<ImagenProductoResponseDto>> ObtenerTodosAsync();
        Task<ImagenProductoResponseDto?> ObtenerPorIdAsync(int id);
        Task<ImagenProductoResponseDto> CrearAsync(ImagenProductoCreateDto dto);
        Task ActualizarAsync(ImagenProductoUpdateDto dto);
        Task EliminarAsync(int id);
    }
}
