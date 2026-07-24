using MesajilApi.DTOs.Pedido;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class PedidoService : IPedidoService
    {
        private readonly IPedidoRepository _repository;
        public PedidoService(IPedidoRepository repository)
        {
            _repository = repository;
        }
        public async Task<List<PedidoResponseDto>> ObtenerTodosAsync()
        {
            var pedidos = await _repository.ObtenerTodosAsync();
            return PedidoMapper.ToResponseDtoList(pedidos);
        }
        public async Task<PedidoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var pedido = await _repository.ObtenerPorIdAsync(id);
            if (pedido == null)
                return null;
            return PedidoMapper.ToResponseDto(pedido);
        }
        public async Task<PedidoResponseDto> CrearAsync(PedidoCreateDto dto)
        {
            if(dto.Total <= 0)
            {
                throw new ArgumentException("El total del pedido debe ser mayor a cero.");
            }
            var pedido = PedidoMapper.ToEntity(dto);
            var creado = await _repository.CrearAsync(pedido);
            return PedidoMapper.ToResponseDto(creado);
        }
        public async Task<PedidoResponseDto?> ActualizarAsync(int id, PedidoUpdateDto dto)
        {
            if(dto.Total <= 0)
            {
                throw new ArgumentException("El total del pedido debe ser mayor que cero.");
            }
            var pedidoExistente = await _repository.ObtenerPorIdAsync(id);
            if (pedidoExistente == null)
                return null;
            pedidoExistente.IdUsuario = dto.IdUsuario;
            pedidoExistente.Total = dto.Total;
            pedidoExistente.EstadoPedido = dto.EstadoPedido;

            var actualizado = await _repository.ActualizarAsync(pedidoExistente);
            return PedidoMapper.ToResponseDto(actualizado);
        }
        public async Task<bool> EliminarAsync(int id)
        {
            return await _repository.EliminarAsync(id);
        }
    }
}
