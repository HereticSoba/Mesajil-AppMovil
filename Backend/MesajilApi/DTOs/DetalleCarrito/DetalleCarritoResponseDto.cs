namespace MesajilApi.DTOs.DetalleCarrito
{
    public class DetalleCarritoResponseDto
    {
        public int IdDetalleCarrito { get; set; }
        public int IdCarrito { get; set; }
        public int IdProducto { get; set; }
        public string NombreProducto { get; set; } = string.Empty;
        public string Marca { get; set; } = string.Empty;
        public string Modelo { get; set; } = string.Empty;
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
        public decimal Subtotal { get; set; }
        public int Stock { get; set; }
    }
}
