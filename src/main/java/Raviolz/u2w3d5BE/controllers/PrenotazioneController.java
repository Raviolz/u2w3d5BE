package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.PrenotazioneDTO;
import Raviolz.u2w3d5BE.payloads.PrenotazioneResDTO;
import Raviolz.u2w3d5BE.services.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(
            PrenotazioneService prenotazioneService
    ) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping("/eventi/{eventoId}/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('UTENTE')")
    public PrenotazioneResDTO savePrenotazione(
            @PathVariable UUID eventoId,
            @RequestBody @Valid PrenotazioneDTO body,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        return this.prenotazioneService.save(
                eventoId,
                body,
                utenteLoggato
        );
    }

    @GetMapping("/prenotazioni/me")
    @PreAuthorize("hasAuthority('UTENTE')")
    public List<PrenotazioneResDTO> getMyBookings(
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        return this.prenotazioneService
                .findMyBookings(utenteLoggato);
    }

    @DeleteMapping("/prenotazioni/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('UTENTE')")
    public void deletePrenotazione(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        this.prenotazioneService.delete(
                id,
                utenteLoggato
        );
    }
}