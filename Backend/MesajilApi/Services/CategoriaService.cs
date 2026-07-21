using MesajilApi.DTOs.Categoria;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class CategoriaService : ICategoriaService
    {
        private readonly ICategoriaRepository _categoriaRepository;

        public CategoriaService(ICategoriaRepository categoriaRepository)
        {
            _categoriaRepository = categoriaRepository;
        }

        public async Task<IEnumerable<CategoriaResponseDto>> ObtenerTodosAsync()
        {
            var categorias = await _categoriaRepository.ObtenerTodosAsync();
            return CategoriaMapper.toResponseDtoList(categorias);
        }

        public async Task<CategoriaResponseDto?> ObtenerPorIdAsync(int id)
        {
            var categoria = await _categoriaRepository.ObtenerPorIdAsync(id);

            if (categoria == null)
                return null;

            return CategoriaMapper.toResponseDto(categoria);
        }

        public async Task<CategoriaResponseDto> CrearAsync(CategoriaCreateDto dto)
        {
            var entidad = CategoriaMapper.toEntity(dto);

            entidad.FechaRegistro = DateTime.Now;
            entidad.Estado = true;

            var nuevaCategoria = await _categoriaRepository.CrearAsync(entidad);

            return CategoriaMapper.toResponseDto(nuevaCategoria);
        }

        public async Task ActualizarAsync(CategoriaUpdateDto dto)
        {
            var entidad = CategoriaMapper.toEntity(dto);

            var categoriaExistente = await _categoriaRepository.ObtenerPorIdAsync(dto.IdCategoria);

            if (categoriaExistente == null)
                throw new Exception("La categoría no existe.");

            entidad.FechaRegistro = categoriaExistente.FechaRegistro;
            entidad.Estado = categoriaExistente.Estado;

            await _categoriaRepository.ActualizarAsync(entidad);
        }

        public async Task EliminarAsync(int id)
        {
            await _categoriaRepository.EliminarAsync(id);
        }
    }
}
