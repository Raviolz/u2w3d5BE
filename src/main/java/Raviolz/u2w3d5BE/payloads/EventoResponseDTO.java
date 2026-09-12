package Raviolz.u2w3d5BE.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record EventoResponseDTO(
        UUID id,
        String titolo,
        String descrizione,
        LocalDate data,
        String luogo,
        int postiTotali,
        int postiDisponibili,
        UUID organizzatoreId,
        String organizzatoreNome,
        String organizzatoreCognome
) {
}