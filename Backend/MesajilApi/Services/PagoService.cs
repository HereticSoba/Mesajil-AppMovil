using MesajilApi.DTOs.Pago;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion.Internal;
using System.Globalization;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;

namespace MesajilApi.Services
{
    public class PagoService : IPagoService
    {
        private readonly ILogger<PagoService> _logger;
        private readonly HttpClient _httpClient;
        private readonly IConfiguration _configuration;
        public PagoService(HttpClient httpClient, IConfiguration configuration, ILogger<PagoService> logger)
        {
            _httpClient = httpClient;
            _configuration = configuration;
            _logger = logger;
        }
        public async Task<PagoResponseDto> ProcesarPagoAsync(PagoRequestDto dto)
        {
            var accessToken = _configuration["MercadoPago:AccessToken"];
            if (string.IsNullOrWhiteSpace(accessToken))
            {
                throw new Exception("No se encontró la configuración de Mercado Pago.");
            }
            if (dto.Monto <= 0)
            {
                throw new Exception("El monto debe ser mayor a cero.");
            }
            if (string.IsNullOrWhiteSpace(dto.Token))
            {
                throw new Exception("El token de tarjeta es obligatorio.");
            }
            var idempotencyKey = Guid.NewGuid().ToString();
            var monto = dto.Monto.ToString("0.00", CultureInfo.InvariantCulture);
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
            return new PagoResponseDto
            {
                IdOrden = idOrden,
                Estado = estado,
                DetalleEstado = detalleEstado,
                Monto = dto.Monto,
                Mensaje = "Solicitud procesada por Mercado Pago."
            };
        }
    }
}
