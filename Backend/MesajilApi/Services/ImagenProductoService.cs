using MesajilApi.DTOs.ImagenProducto;
using MesajilApi.Mappings;
using MesajilApi.Repositories;
using CloudinaryDotNet;
using CloudinaryDotNet.Actions;

namespace MesajilApi.Services
{
    public class ImagenProductoService : IImagenProductoService
    {
        private readonly IImagenProductoRepository _imagenRepository;
        private readonly Cloudinary _cloudinary;

        public ImagenProductoService(
            IImagenProductoRepository imagenRepository,
            IConfiguration configuration)
        {
            _imagenRepository = imagenRepository;

            var cloudName = configuration["CLOUDINARY_CLOUD_NAME"];
            var apiKey = configuration["CLOUDINARY_API_KEY"];
            var apiSecret = configuration["CLOUDINARY_API_SECRET"];

            var account = new Account(
                cloudName,
                apiKey,
                apiSecret
            );

            _cloudinary = new Cloudinary(account);
            _cloudinary.Api.Secure = true;
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

        public async Task<ImagenProductoResponseDto> CrearAsync(
            ImagenProductoCreateDto dto)
        {
            var uploadParams = new ImageUploadParams
            {
                File = new FileDescription(
                    dto.Imagen.FileName,
                    dto.Imagen.OpenReadStream()
                ),
                Folder = "mesajil/productos"
            };

            var uploadResult = await _cloudinary.UploadAsync(uploadParams);

            if (uploadResult.Error != null)
            {
                throw new Exception(
                    $"Error al subir imagen a Cloudinary: {uploadResult.Error.Message}"
                );
            }

            var imagen = ImagenProductoMapper.ToEntity(dto);

            imagen.UrlImagen = uploadResult.SecureUrl.ToString();

            var nuevaImagen = await _imagenRepository.CrearAsync(imagen);

            return ImagenProductoMapper.ToResponseDto(nuevaImagen);
        }

        public async Task ActualizarAsync(
            ImagenProductoUpdateDto dto)
        {
            var imagenExistente =
                await _imagenRepository.ObtenerPorIdAsync(dto.IdImagen);

            if (imagenExistente == null)
                throw new Exception("La imagen no existe.");

            imagenExistente.IdProducto = dto.IdProducto;
            imagenExistente.Principal = dto.Principal;

            if (dto.Imagen != null)
            {
                var uploadParams = new ImageUploadParams
                {
                    File = new FileDescription(
                        dto.Imagen.FileName,
                        dto.Imagen.OpenReadStream()
                    ),
                    Folder = "mesajil/productos"
                };

                var uploadResult =
                    await _cloudinary.UploadAsync(uploadParams);

                if (uploadResult.Error != null)
                {
                    throw new Exception(
                        $"Error al subir imagen a Cloudinary: {uploadResult.Error.Message}"
                    );
                }

                imagenExistente.UrlImagen =
                    uploadResult.SecureUrl.ToString();
            }

            await _imagenRepository.ActualizarAsync(imagenExistente);
        }

        public async Task EliminarAsync(int id)
        {
            var imagen =
                await _imagenRepository.ObtenerPorIdAsync(id);

            if (imagen == null)
                throw new Exception("La imagen no existe.");

            /*
             * Si la imagen pertenece a Cloudinary,
             * posteriormente podemos eliminarla también
             * desde Cloudinary utilizando su PublicId.
             */

            await _imagenRepository.EliminarAsync(id);
        }
    }
}
