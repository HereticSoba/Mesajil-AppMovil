namespace MesajilApi.DTOs.DetalleCarrito
{
    public class DetalleCarritoResponseDto
    {
        public int IdDetalleCarrito { get; set; }
        public int IdCarrito { get; set; }
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
        public decimal Subtotal { get; set; }
    }
}
