namespace MesajilApi.DTOs.ImagenProducto
{
    public class ImagenProductoResponseDto
    {
        public int IdImagen {  get; set; }
        public int IdProducto { get; set; }
        public string UrlImagen { get; set; } = string.Empty;
        public bool Principal { get; set; }
    }
}
