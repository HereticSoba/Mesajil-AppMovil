using MesajilApi.DTOs.Pago;
using MesajilApi.Repositories;
using System.Globalization;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;
using MesajilApi.DTOs.Pedido;

namespace MesajilApi.Services
{
    public class PagoService : IPagoService
    {
        private readonly ILogger<PagoService> _logger;
        private readonly HttpClient _httpClient;
        private readonly IConfiguration _configuration;
        private readonly ICarritoRepository _carritoRepository;
        private readonly IDetalleCarritoRepository _detalleCarritoRepository;
        private readonly IInventarioRepository _inventarioRepository;
        private readonly IPedidoService _pedidoService;
        public PagoService(HttpClient httpClient, IConfiguration configuration, ILogger<PagoService> logger, ICarritoRepository carritoRepository, IDetalleCarritoRepository detalleCarritoRepository, IInventarioRepository inventarioRepository, IPedidoService pedidoService)
        {
            _httpClient = httpClient;
            _configuration = configuration;
            _logger = logger;
            _carritoRepository = carritoRepository;
            _detalleCarritoRepository = detalleCarritoRepository;
            _inventarioRepository = inventarioRepository;
            _pedidoService = pedidoService;
        }
        public async Task<PagoResponseDto> ProcesarPagoAsync(int idUsuario, PagoRequestDto dto)
        {
            var accessToken = _configuration["MercadoPago:AccessToken"];
            if (string.IsNullOrWhiteSpace(accessToken))
            {
                throw new Exception("No se encontró la configuración de Mercado Pago.");
            }
            if (string.IsNullOrWhiteSpace(dto.Token))
            {
                throw new Exception("El token de tarjeta es obligatorio.");
            }
            var tipoEntrega = dto.TipoEntrega.Trim();
            if (!tipoEntrega.Equals(
                "Delivery",
                StringComparison.OrdinalIgnoreCase) &&
                !tipoEntrega.Equals(
                    "Recojo",
                    StringComparison.OrdinalIgnoreCase))
            {
                throw new Exception(
                    "El tipo de entrega debe ser Delivery o Recojo.");
            }
            tipoEntrega = tipoEntrega.Equals(
                "Delivery",
                StringComparison.OrdinalIgnoreCase)
                ? "Delivery"
                : "Recojo";
            decimal costoEnvio;
            if (tipoEntrega == "Delivery")
            {
                if (string.IsNullOrWhiteSpace(dto.DireccionEntrega))
                {
                    throw new Exception(
                        "Debe ingresar una dirección para el delivery.");
                }
                costoEnvio = 10.00m;
            }
            else
            {
                costoEnvio = 0.00m;
            }
            var carrito = await _carritoRepository.ObtenerPorUsuarioAsync(idUsuario);
            if (carrito == null)
            {
                throw new Exception("No se encontró un carrito activo.");
            }
            var detalles = await _detalleCarritoRepository.ObtenerPorCarritoAsync(carrito.IdCarrito);
            if (!detalles.Any())
            {
                throw new Exception("El carrito está vacío.");
            }
            foreach (var item in detalles)
            {
                var inventario = await _inventarioRepository.ObtenerPorProductoAsync(item.IdProducto);
                if (inventario == null)
                {
                    throw new Exception($"No existe inventario para el producto {item.IdProducto}");
                }
                if (inventario.StockActual < item.Cantidad)
                {
                    throw new Exception($"Stock insuficiente para el producto {item.IdProducto}");
                }
            }
            decimal subtotal = detalles.Sum(d => d.Subtotal);
            decimal montoReal = subtotal + costoEnvio;
            var idempotencyKey = Guid.NewGuid().ToString();
            var monto = montoReal.ToString("0.00", CultureInfo.InvariantCulture);
            var body = new
            {
                type = "online",
                processing_mode = "automatic",
                total_amount = monto,
                external_reference = $"MESAJIL-{Guid.NewGuid()}",
                payer = new
                {
                    email = dto.Email,
                    identification = new
                    {
                        type = dto.TipoDocumento,
                        number = dto.NumeroDocumento
                    }
                },
                transactions = new
                {
                    payments = new[]
    {
                        new
                        {
                            amount = monto,
                            payment_method = new
                            {
                                id = dto.MetodoPago,
                                type = dto.TipoMetodoPago,
                                token = dto.Token,
                                installments = dto.Cuotas
                            }
                        }
                    }
                }
            };
            var json = JsonSerializer.Serialize(body);
            using var request = new HttpRequestMessage(HttpMethod.Post, "https://api.mercadopago.com/v1/orders");
            request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
            request.Headers.Add("x-idempotency-key", idempotencyKey);
            request.Content = new StringContent(json, Encoding.UTF8, "application/json");
            var response = await _httpClient.SendAsync(request);
            var responseContent = await response.Content.ReadAsStringAsync();
            _logger.LogInformation("Respuesta Mercado Pago - Status: {StatusCode} - Body: {ResponseContent}",
                (int)response.StatusCode,
                responseContent
                );
            if (!response.IsSuccessStatusCode)
            {
                throw new Exception(
                    $"Mercado Pago rechazó la solicitud." +
                    $"Código: {(int)response.StatusCode}." +
                    $"Detalle: {responseContent}");
            }
            using var document = JsonDocument.Parse(responseContent);
            var root = document.RootElement;
            var idOrden = root.TryGetProperty("id", out var id)
                ? id.GetString() ?? string.Empty
                : string.Empty;
            var estado = root.TryGetProperty("status", out var status)
                ? status.GetString() ?? string.Empty
                : string.Empty;
            var detalleEstado = root.TryGetProperty("status_detail", out var statusDetail)
                ? statusDetail.GetString()
                : null;
            if(!estado.Equals("processed", StringComparison.OrdinalIgnoreCase))
            {
                return new PagoResponseDto
                {
                    IdOrden = idOrden,
                    Estado = estado,
                    DetalleEstado = detalleEstado,
                    Monto = montoReal,
                    IdPedido = null,
                    Mensaje = "El pago no fue procesado. Pedido no generado."
                };
            }
            var pedidoFinalizado = await _pedidoService.FinalizarCompraAsync(
                idUsuario,
                new FinalizarCompraDto
                {
                    TipoEntrega = dto.TipoEntrega,
                    DireccionEntrega = dto.DireccionEntrega
                },
                "Pagado",
                idOrden
                );
            return new PagoResponseDto
            {
                IdOrden = idOrden,
                Estado = estado,
                DetalleEstado = detalleEstado,
                Monto = montoReal,
                IdPedido = pedidoFinalizado.IdPedido,
                Mensaje = "Solicitud procesada por Mercado Pago."
            };
        }
    }
}
