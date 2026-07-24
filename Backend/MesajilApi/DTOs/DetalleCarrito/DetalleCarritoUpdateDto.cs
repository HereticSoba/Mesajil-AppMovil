namespace MesajilApi.DTOs.DetalleCarrito
{
    public class DetalleCarritoUpdateDto
    {
        public int IdDetalleCarrito{ get; set; }
        public int IdCarrito { get; set; }
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
    }
}
