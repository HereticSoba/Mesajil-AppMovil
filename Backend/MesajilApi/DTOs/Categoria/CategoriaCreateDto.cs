using System.ComponentModel.DataAnnotations;

namespace MesajilApi.DTOs.Categoria
{
    public class CategoriaCreateDto
    {
        [Required]
        [StringLength(100)]
        public string Nombre { get; set; } = string.Empty;
        [StringLength(300)]
        public string? Descripcion { get; set; }
    }
}
