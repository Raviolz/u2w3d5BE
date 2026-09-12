package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.UtenteResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    @GetMapping("/me")
    public UtenteResponseDTO getMyProfile(
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        return new UtenteResponseDTO(
                utenteLoggato.getId(),
                utenteLoggato.getNome(),
                utenteLoggato.getCognome(),
                utenteLoggato.getEmail(),
                utenteLoggato.getRuolo()
        );
    }
}