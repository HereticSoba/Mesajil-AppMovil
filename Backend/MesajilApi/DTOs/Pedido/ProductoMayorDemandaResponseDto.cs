namespace MesajilApi.DTOs.Pedido
{
    public class ProductoMayorDemandaResponseDto
    {
        public int IdProducto { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public int CantidadVendida { get; set; }
    }
}
