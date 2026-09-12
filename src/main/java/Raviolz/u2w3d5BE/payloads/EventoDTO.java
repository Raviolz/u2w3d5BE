package Raviolz.u2w3d5BE.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventoDTO(

        @NotBlank(message = "Titolo obbligatorio")
        @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
        String titolo,

        @NotBlank(message = "Descrizione obbligatoria")
        @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
        String descrizione,

        @NotNull(message = "Data obbligatoria")
        @FutureOrPresent(message = "La data dell'evento non può essere nel passato")
        LocalDate data,

        @NotBlank(message = "Luogo obbligatorio")
        @Size(max = 150, message = "Il luogo non può superare i 150 caratteri")
        String luogo,

        @NotNull(message = "Numero di posti obbligatorio")
        @Min(value = 1, message = "L'evento deve avere almeno un posto disponibile")
        Integer postiTotali

) {
}