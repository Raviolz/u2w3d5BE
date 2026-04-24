package Raviolz.u2w3d5BE.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventoDTO(
        @NotBlank(message = "Titolo obbligatorio")
        String titolo,

        @NotBlank(message = "Descrizione obbligatoria")
        String descrizione,

        @NotNull(message = "Data obbligatoria")
        @Future(message = "Inutile registrare un evento gia' passato")
        LocalDate data,

        @NotBlank(message = "Luogo obbligatorio")
        String luogo,

        @Min(value = 9, message = "Non teniamo eventi cosi piccoli")
        int postiTotali
) {
}
