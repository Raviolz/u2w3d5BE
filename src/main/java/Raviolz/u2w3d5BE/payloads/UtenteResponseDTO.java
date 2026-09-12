package Raviolz.u2w3d5BE.payloads;

import Raviolz.u2w3d5BE.entities.Ruolo;

import java.util.UUID;

public record UtenteResponseDTO(

        UUID id,
        String nome,
        String cognome,
        String email,
        Ruolo ruolo

) {
}