using MesajilApi.DTOs.DetalleCarrito;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class DetalleCarritoService : IDetalleCarritoService
    {
        private readonly IDetalleCarritoRepository _repository;
        public DetalleCarritoService(IDetalleCarritoRepository repository)
        {
            _repository = repository;
        }
        public async Task<List<DetalleCarritoResponseDto>> ObtenerTodosAsync()
        {
            var detalles = await _repository.ObtenerTodosAsync();
            return DetalleCarritoMapper.ToResponseDtoList(detalles);
        }
        public async Task<DetalleCarritoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var detalle = await _repository.ObtenerPorIdAsync(id);
            if(detalle == null)
                return null;
            return DetalleCarritoMapper.ToResponseDto(detalle);
        }
        public async Task<DetalleCarritoResponseDto> CrearAsync(DetalleCarritoCreateDto dto)
        {
            if(dto.Cantidad <= 0)
            {
                throw new ArgumentException("La cantidad debe ser mayor a cero.");
            }
            var detalle = DetalleCarritoMapper.ToEntity(dto);
            var creado = await _repository.CrearAsync(detalle);
            return DetalleCarritoMapper.ToResponseDto(creado);
        }
        public async Task<DetalleCarritoResponseDto?> ActualizarAsync(int id, DetalleCarritoUpdateDto dto)
        {
            if (dto.Cantidad <= 0)
            {
                throw new ArgumentException("La cantidad debe ser mayor a cero.");
            }
            var detalleExistente = await _repository.ObtenerPorIdAsync(id);
            if(detalleExistente == null)
                return null;
            detalleExistente.IdCarrito = dto.IdCarrito;
            detalleExistente.IdProducto = dto.IdProducto;
            detalleExistente.Cantidad = dto.Cantidad;
            detalleExistente.PrecioUnitario = dto.PrecioUnitario;

            detalleExistente.Subtotal = dto.Cantidad * dto.PrecioUnitario;
            var actualizado = await _repository.ActualizarAsync(detalleExistente);
            return DetalleCarritoMapper.ToResponseDto(actualizado);
        }
        public async Task<bool> EliminarAsync(int id)
        {
            return await _repository.EliminarAsync(id);
        }
    }
}
