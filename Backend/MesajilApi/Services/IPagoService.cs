using MesajilApi.DTOs.Pago;

namespace MesajilApi.Services
{
    public interface IPagoService
    {
        Task<PagoResponseDto> ProcesarPagoAsync(int idUsuario, PagoRequestDto dto);
    }
}
