namespace MesajilApi.DTOs.Categoria
{
    public class CategoriaResponseDto
    {
        public int IdCategoria { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string? Descripcion {  get; set; }
        public DateTime FechaRegistro { get; set; }
        public bool Estado {  get; set; }
    }
}
