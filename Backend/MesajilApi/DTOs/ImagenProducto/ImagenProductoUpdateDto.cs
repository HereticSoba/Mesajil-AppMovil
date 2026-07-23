using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.ImagenProducto
{
    public class ImagenProductoUpdateDto
    {
        [Required]
        public int IdImagen {  get; set; }
        [Required]
        public int IdProducto { get; set; }
        public IFormFile? Imagen {  get; set; }
        public bool Principal {  get; set; }
    }
}
