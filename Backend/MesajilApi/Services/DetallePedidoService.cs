using MesajilApi.DTOs.DetallePedido;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class DetallePedidoService : IDetallePedidoService
    {
        private readonly IDetallePedidoRepository _repository;
        public DetallePedidoService(IDetallePedidoRepository repository)
        {
            _repository = repository;
        }
        public async Task<List<DetallePedidoResponseDto>> ObtenerTodosAsync()
        {
            var detalles = await _repository.ObtenerTodosAsync();
            return DetallePedidoMapper.ToResponseDtoList(detalles);
        }
        public async Task<DetallePedidoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var detalle = await _repository.ObtenerPorIdAsync(id);
            if(detalle  == null)
                return null;
            return DetallePedidoMapper.ToResponseDto(detalle);
        }
        public async Task<DetallePedidoResponseDto> CrearAsync(DetallePedidoCreateDto dto)
        {
            if(dto.Cantidad <= 0)
            {
                throw new ArgumentException("La cantidad debe ser mayor a cero.");
            }
            if(dto.PrecioUnitario <= 0)
            {
                throw new ArgumentException("El precio unitario debe ser mayor a cero.");
            }
            var detalle = DetallePedidoMapper.ToEntity(dto);
            var creado = await _repository.CrearAsync(detalle);
            return DetallePedidoMapper.ToResponseDto(creado);
        }
        public async Task<DetallePedidoResponseDto?> ActualizarAsync(int id, DetallePedidoUpdateDto dto)
        {
            if(dto.Cantidad <= 0)
            {
                throw new ArgumentException("La cantidad debe ser mayor a cero.");
            }
            if(dto.PrecioUnitario <= 0)
            {
                throw new ArgumentException("El precio unitario debe ser mayor a cero.");
            }
            var detalleExistente = await  _repository.ObtenerPorIdAsync(id);
            if(detalleExistente == null)
                return null;
            detalleExistente.IdPedido = dto.IdPedido;
            detalleExistente.IdProducto = dto.IdProducto;
            detalleExistente.Cantidad = dto.Cantidad;
            detalleExistente.PrecioUnitario = dto.PrecioUnitario;
            detalleExistente.Subtotal = dto.Cantidad * dto.PrecioUnitario;

            var actualizado = await _repository.ActualizarAsync(detalleExistente);
            return DetallePedidoMapper.ToResponseDto(actualizado);
        }
        public async Task<bool> EliminarAsync(int id)
        {
            return await _repository.EliminarAsync(id);
        }
    }
}
