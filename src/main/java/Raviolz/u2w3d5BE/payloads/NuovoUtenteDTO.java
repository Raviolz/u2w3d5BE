package Raviolz.u2w3d5BE.payloads;

import Raviolz.u2w3d5BE.entities.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NuovoUtenteDTO(
        @NotBlank(message = "Nome obbligatorio")
        String nome,
        @NotBlank(message = "Cognome obbligatorio")
        String cognome,
        @Email(message = "Email non valida")
        String email,
        @NotBlank(message = "Password obbligatoria")
        String password,
        @NotBlank(message = "Seleziona come vuoi registrarti")
        Ruolo ruolo
) {
}