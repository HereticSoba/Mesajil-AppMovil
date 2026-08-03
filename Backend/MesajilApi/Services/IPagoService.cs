using MesajilApi.DTOs.Pago;

namespace MesajilApi.Services
{
    public interface IPagoService
    {
        Task<PagoResponseDto> ProcesarPagoAsync(PagoRequestDto dto);
    }
}
