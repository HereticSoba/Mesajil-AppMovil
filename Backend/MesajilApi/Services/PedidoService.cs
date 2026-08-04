using MesajilApi.Data;
using MesajilApi.DTOs.Pedido;
using MesajilApi.Mappings;
using MesajilApi.Repositories;
using MesajilApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MesajilApi.Services
{
    public class PedidoService : IPedidoService
    {
        private readonly IPedidoRepository _repository;
        private readonly ICarritoRepository _carritoRepository;
        private readonly IDetalleCarritoRepository _detalleCarritoRepository;
        private readonly DbMesajilContext _context;
        private readonly IInventarioRepository _inventarioRepository;
        public PedidoService(
            IPedidoRepository repository,
            ICarritoRepository carritoRepository,
            IDetalleCarritoRepository detalleCarritoRepository,
            IInventarioRepository inventarioRepository,
            DbMesajilContext context)
        {
            _repository = repository;
            _carritoRepository = carritoRepository;
            _detalleCarritoRepository = detalleCarritoRepository;
            _inventarioRepository = inventarioRepository;
            _context = context;
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
            if (dto.Total <= 0)
            {
                throw new ArgumentException("El total del pedido debe ser mayor a cero.");
            }
            var pedido = PedidoMapper.ToEntity(dto);
            var creado = await _repository.CrearAsync(pedido);
            return PedidoMapper.ToResponseDto(creado);
        }
        public async Task<PedidoResponseDto?> ActualizarAsync(int id, PedidoUpdateDto dto)
        {
            if (dto.Total <= 0)
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
        public async Task<PedidoFinalizadoResponseDto> FinalizarCompraAsync(int idUsuario, FinalizarCompraDto dto, string estadoPago = "Pendiente", string? idOrdenMercadoPago = null)
        {
            await using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var tipoEntrega = dto.TipoEntrega.Trim();
                if(!tipoEntrega.Equals("Delivery", StringComparison.OrdinalIgnoreCase) &&
                   !tipoEntrega.Equals("Recojo", StringComparison.OrdinalIgnoreCase))
                {
                    throw new ArgumentException("El tipo de entrega debe ser Delivery o Recojo.");
                }
                tipoEntrega = tipoEntrega.Equals(
                    "Delivery",
                    StringComparison.OrdinalIgnoreCase) ? "Delivery" : "Recojo";

                decimal costoEnvio;
                string? direccionEntrega;
                string? tiendaRecojo;
                if (tipoEntrega == "Delivery")
                {
                    if(string.IsNullOrWhiteSpace(dto.DireccionEntrega))
                    {
                        throw new Exception("Debe ingresar una dirección para el delivery.");
                    }
                    costoEnvio = 10.00m;
                    direccionEntrega = dto.DireccionEntrega.Trim();
                    tiendaRecojo = null;
                }
                else
                {
                    costoEnvio = 0.00m;
                    direccionEntrega = null;
                    tiendaRecojo = "Mesajil - Compuplaza Lima";
                }
                var carrito = await _carritoRepository.ObtenerPorUsuarioAsync(idUsuario);
                if (carrito == null)
                    throw new Exception("No se encontró un carrito activo.");

                var detalles = await _detalleCarritoRepository.ObtenerPorCarritoAsync(carrito.IdCarrito);
                if (!detalles.Any())
                    throw new Exception("El carrito está vacío.");
                foreach(var item in detalles)
                {
                    var inventario = await _inventarioRepository
                        .ObtenerPorProductoAsync(item.IdProducto);
                    if (inventario == null)
                        throw new Exception($"No existe inventario para el producto {item.IdProducto}."
                        );
                    if (inventario.StockActual < item.Cantidad)
                        throw new Exception(
                            $"Stock insuficiente para el producto {inventario.Producto!.Nombre}.");
                    
                }
                decimal subtotal = detalles.Sum(d => d.Subtotal);
                decimal total = subtotal + costoEnvio;

                var pedido = new Pedido
                {
                    IdUsuario = idUsuario,
                    FechaPedido = DateTime.Now,
                    Total = total,
                    EstadoPedido = "Pendiente",
                    TipoEntrega = tipoEntrega,
                    DireccionEntrega = direccionEntrega,
                    TiendaRecojo = tiendaRecojo,
                    CostoEnvio = costoEnvio,
                    EstadoPago = estadoPago,
                    IdOrdenMercadoPago = idOrdenMercadoPago
                };
                _context.Pedidos.Add(pedido);
                await _context.SaveChangesAsync();
                foreach (var item in detalles)
                {
                    _context.DetallePedidos.Add(new DetallePedido
                    {
                        IdPedido = pedido.IdPedido,
                        IdProducto = item.IdProducto,
                        Cantidad = item.Cantidad,
                        PrecioUnitario = item.PrecioUnitario,
                        Subtotal = item.Subtotal
                    });
                }
                foreach (var item in detalles)
                {
                    var inventario = await _inventarioRepository.ObtenerPorProductoAsync(item.IdProducto);
                    if(inventario != null)
                    {
                        inventario.StockActual -= item.Cantidad;
                        inventario.UltimaActualizacion = DateTime.Now;
                        await _inventarioRepository.ActualizarAsync(inventario);
                    }
                }
                _context.DetalleCarritos.RemoveRange(detalles);

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                return new PedidoFinalizadoResponseDto
                {
                    IdPedido = pedido.IdPedido,
                    Total = pedido.Total,
                    Mensaje = "Pedido registrado exitosamente."
                };
            }
            catch
            {
                await transaction.RollbackAsync();
                throw;
            }
        }
        public async Task<List<PedidoResponseDto>> ObtenerMisPedidosAsync(int idUsuario)
        {
            var pedidos = await _repository.ObtenerPorUsuarioAsync(idUsuario);
            return PedidoMapper.ToResponseDtoList(pedidos);
        }
    }
}