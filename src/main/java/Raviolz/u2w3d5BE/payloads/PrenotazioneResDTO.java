package Raviolz.u2w3d5BE.payloads;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PrenotazioneResDTO(

        UUID id,
        int postiPrenotati,
        LocalDateTime dataPrenotazione,

        UUID eventoId,
        String eventoTitolo,
        LocalDate eventoData,
        String eventoLuogo,

        UUID utenteId

) {
}