using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.ImagenProducto
{
    public class ImagenProductoCreateDto
    {
        [Required]
        public int IdProducto { get; set; }
        [Required]
        public IFormFile Imagen { get; set; } = null!;
        public bool Principal {  get; set; }
    }
}
