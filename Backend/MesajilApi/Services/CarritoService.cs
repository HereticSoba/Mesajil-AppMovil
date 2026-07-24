using MesajilApi.DTOs.Carrito;
using MesajilApi.Mappings;
using MesajilApi.Repositories;

namespace MesajilApi.Services
{
    public class CarritoService : IcarritoService
    {
        private readonly ICarritoRepository _repository;
        public CarritoService(ICarritoRepository repository)
        {
            _repository = repository;
        }
        public async Task<List<CarritoResponseDto>> ObtenerTodosAsync()
        {
            var carritos = await _repository.ObtenerTodosAsync();
            return CarritoMapper.ToResponseDtoList(carritos);
        }
        public async Task<CarritoResponseDto?> ObtenerPorIdAsync(int id)
        {
            var carrito = await _repository.ObtenerPorIdAsync(id);
            if(carrito == null)
                return null;
            return CarritoMapper.ToResponseDto(carrito);
        }
        public async Task<CarritoResponseDto> CrearAsync(CarritoCreateDto dto)
        {
            var carrito = CarritoMapper.toEntity(dto);
            var creado = await _repository.CrearAsync(carrito);
            return CarritoMapper.ToResponseDto(creado);
        }
        public async Task<CarritoResponseDto?> ActualizarAsync(int id, CarritoUpdateDto dto)
        {
            var carritoExistente = await _repository.ObtenerPorIdAsync(id);
            if(carritoExistente == null)
                return null;
            carritoExistente.IdUsuario = dto.IdUsuario;
            carritoExistente.Estado = dto.Estado;

            var actualizado = await _repository.ActualizarAsync(carritoExistente);
            return CarritoMapper.ToResponseDto(actualizado);
        }
        public async Task<bool> EliminarAsync(int id)
        {
            return await _repository.EliminarAsync(id);
        }
    }
}
