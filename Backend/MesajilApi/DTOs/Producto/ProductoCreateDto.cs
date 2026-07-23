using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Producto
{
    public class ProductoCreateDto
    {
        [Required]
        public int IdCategoria { get; set; }
        [Required]
        [StringLength(120)]
        public string Nombre { get; set; } = string.Empty;
        public string? Descripcion { get; set; }
        [Required]
        [StringLength(80)]
        public string Marca { get; set; } = string.Empty;
        [Required]
        [StringLength(80)]
        public string Modelo {  get; set; } = string.Empty;
        [Required]
        public decimal Precio { get; set; }
    }
}
