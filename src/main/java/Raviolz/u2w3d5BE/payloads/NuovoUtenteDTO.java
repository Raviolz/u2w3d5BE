package Raviolz.u2w3d5BE.payloads;

import Raviolz.u2w3d5BE.entities.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NuovoUtenteDTO(

        @NotBlank(message = "Nome obbligatorio")
        @Size(max = 50, message = "Il nome non può superare i 50 caratteri")
        String nome,

        @NotBlank(message = "Cognome obbligatorio")
        @Size(max = 50, message = "Il cognome non può superare i 50 caratteri")
        String cognome,

        @NotBlank(message = "Email obbligatoria")
        @Email(message = "Email non valida")
        String email,

        @NotBlank(message = "Password obbligatoria")
        @Size(
                min = 8,
                max = 100,
                message = "La password deve contenere tra 8 e 100 caratteri"
        )
        String password,

        @NotNull(message = "Seleziona come vuoi registrarti")
        Ruolo ruolo

) {
}