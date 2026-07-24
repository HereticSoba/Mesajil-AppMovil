namespace MesajilApi.DTOs.Carrito
{
    public class CarritoResponseDto
    {
        public int IdCarrito { get; set; }
        public int IdUsuario { get; set; }
        public DateTime FechaCreacion { get; set; }
        public bool Estado { get; set; }
    }
}
