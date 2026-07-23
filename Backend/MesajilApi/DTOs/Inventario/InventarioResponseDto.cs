namespace MesajilApi.DTOs.Inventario
{
    public class InventarioResponseDto
    {
        public int IdInventario { get; set; }
        public int IdProducto { get; set; }
        public int StockActual { get; set; }
        public int StockMinimo { get; set; }
        public DateTime UlitmaActualizacion { get; set; }
    }
}
