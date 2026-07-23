using MesajilApi.DTOs.ImagenProducto;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class ImagenProductoService : IImagenProductoService
    {
        private readonly IImagenProductoRepository _imagenRepository;
        private readonly IWebHostEnvironment _environment;
        public ImagenProductoService(
            IImagenProductoRepository imagenRepository,
            IWebHostEnvironment environment)
        {
            _imagenRepository = imagenRepository;
            _environment = environment;
        }
        public async Task<IEnumerable<ImagenProductoResponseDto>> ObtenerTodosAsync()
        {
            var imagenes = await _imagenRepository.ObtenerTodosAsync();
            return ImagenProductoMapper.ToResponseDtoList(imagenes);
        }
        public async Task<ImagenProductoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var imagen = await _imagenRepository.ObtenerPorIdAsync(id);
            if (imagen == null)
                return null;
            return ImagenProductoMapper.ToResponseDto(imagen);
        }
        public async Task<ImagenProductoResponseDto> CrearAsync(ImagenProductoCreateDto dto)
        {
            var carpetaImagenes = Path.Combine(
                _environment.WebRootPath,
                "images",
                "productos");
            if (!Directory.Exists(carpetaImagenes))
            {
                Directory.CreateDirectory(carpetaImagenes);
            }
            var extension = Path.GetExtension(dto.Imagen.FileName);
            var nombreArchivo = $"{Guid.NewGuid()}{extension}";
            var rutaCompleta = Path.Combine(
                carpetaImagenes,
                nombreArchivo);
            using (var stream = new FileStream(rutaCompleta, FileMode.Create))
            {
                await dto.Imagen.CopyToAsync(stream);
            }
            var imagen = ImagenProductoMapper.ToEntity(dto);
            imagen.UrlImagen = $"/images/productos/{nombreArchivo}";
            var nuevaImagen = await _imagenRepository.CrearAsync(imagen);
            return ImagenProductoMapper.ToResponseDto(nuevaImagen);
        }
        public async Task ActualizarAsync(ImagenProductoUpdateDto dto)
        {
            var imagenExistente = await _imagenRepository.ObtenerPorIdAsync(dto.IdImagen);
            if (imagenExistente == null)
                throw new Exception("La imagen no existe.");
            imagenExistente.IdProducto = dto.IdProducto;
            imagenExistente.Principal = dto.Principal;
            if (dto.Imagen != null)
            {
                var rutaAnterior = Path.Combine(
                    _environment.WebRootPath,
                    imagenExistente.UrlImagen
                    .TrimStart('/')
                    .Replace('/', Path.DirectorySeparatorChar));
                if (File.Exists(rutaAnterior))
                {
                    File.Delete(rutaAnterior);
                }
                var extension = Path.GetExtension(dto.Imagen.FileName);
                var nombreArchivo = $"{Guid.NewGuid()}{extension}";
                var carpetaImagenes = Path.Combine(
                    _environment.WebRootPath,
                    "images",
                    "productos");
                var rutaCompleta = Path.Combine(
                    carpetaImagenes,
                    nombreArchivo);
                using (var stream = new FileStream(rutaCompleta, FileMode.Create))
                {
                    await dto.Imagen.CopyToAsync(stream);
                }
                imagenExistente.UrlImagen = $"/images/productos/{nombreArchivo}";
            }
            await _imagenRepository.ActualizarAsync(imagenExistente);
        }
        public async Task EliminarAsync(int id)
        {
            var imagen = await _imagenRepository.ObtenerPorIdAsync(id);
            if (imagen == null)
                throw new Exception("La imagen no existe.");

            var rutaImagen = Path.Combine(
                _environment.WebRootPath,
                imagen.UrlImagen
                .TrimStart('/')
                .Replace('/', Path.DirectorySeparatorChar));
            if (File.Exists(rutaImagen))
            {
                File.Delete(rutaImagen);
            }
            await _imagenRepository.EliminarAsync(id);
        }
    }
}
