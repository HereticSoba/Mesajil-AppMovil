namespace MesajilApi.DTOs.Inventario
{
    public class InventarioCreateDto
    {
        public int IdProducto { get; set; }
        public int StockActual { get; set; }
        public int StockMinimo { get; set; }
    }
}
