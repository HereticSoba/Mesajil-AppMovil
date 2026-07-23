using MesajilApi.DTOs.Inventario;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class InventarioService : IInventarioService
    {
        private readonly IInventarioRepository _inventarioRepository;
        public InventarioService(IInventarioRepository inventarioRepository)
        {
            _inventarioRepository = inventarioRepository;
        }
        public async Task<IEnumerable<InventarioResponseDto>> ObtenerTodosAsync()
        {
            var inventarios = await _inventarioRepository.ObtenerTodosAsync();
            return InventarioMapper.ToResponseDtolist(inventarios);
        }
        public async Task<InventarioResponseDto?> ObtenerPorIdAsync(int id)
        {
            var inventario = await _inventarioRepository.ObtenerPorIdAsync(id);
            if (inventario == null)
                return null;
                return InventarioMapper.ToResponseDto(inventario);
        }
        public async Task<InventarioResponseDto> CrearAsync(InventarioCreateDto dto)
        {
            var inventario = InventarioMapper.ToEntity(dto);
            await _inventarioRepository.CrearAsync(inventario);
            return InventarioMapper.ToResponseDto(inventario);
        }
        public async Task ActualizarAsync(InventarioUpdateDto dto)
        {
            var inventarioExistente = await _inventarioRepository.ObtenerPorIdAsync(dto.IdInventario);
            if (inventarioExistente == null)
                throw new Exception("Inventario no encontrado.");
            inventarioExistente.IdProducto = dto.IdProducto;
            inventarioExistente.StockActual = dto.StockActual;
            inventarioExistente.StockMinimo = dto.StockMinimo;
            inventarioExistente.UltimaActualizacion = DateTime.Now;
            
            await _inventarioRepository.ActualizarAsync(inventarioExistente);
        }
        public async Task EliminarAsync(int id)
        {
            var inventario = await _inventarioRepository.ObtenerPorIdAsync(id);
            if (inventario == null)
                throw new Exception("Inventario no encontrado.");
            await _inventarioRepository.EliminarAsync(id);
        }
    }
}
