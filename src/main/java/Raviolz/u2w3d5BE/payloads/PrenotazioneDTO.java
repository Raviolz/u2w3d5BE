package Raviolz.u2w3d5BE.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PrenotazioneDTO(

        @NotNull(message = "Numero di posti obbligatorio")
        @Min(value = 1, message = "Devi prenotare almeno un posto")
        Integer postiPrenotati

) {
}