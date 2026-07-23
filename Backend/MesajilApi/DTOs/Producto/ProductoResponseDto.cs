namespace MesajilApi.DTOs.Producto
{
    public class ProductoResponseDto
    {
        public int IdProducto { get; set; }
        public int IdCategoria { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string? Descripcion { get; set; }
        public string Marca {  get; set; } = string.Empty;
        public string Modelo {  get; set; } = string.Empty;
        public decimal Precio { get; set; }
        public bool Estado { get; set; }
        public DateTime FechaRegistro { get; set; }
    }
}
