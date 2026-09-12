package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.payloads.EventoResponseDTO;
import Raviolz.u2w3d5BE.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    private final EventoService evService;

    public EventoController(EventoService evService) {
        this.evService = evService;
    }

    @GetMapping
    public List<EventoResponseDTO> getEventi() {
        return this.evService.findAll();
    }

    @GetMapping("/{id}")
    public EventoResponseDTO getEventoById(
            @PathVariable UUID id
    ) {
        return this.evService.findByIdResponse(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public EventoResponseDTO saveEvento(
            @RequestBody @Valid EventoDTO body,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        return this.evService.save(
                body,
                utenteLoggato
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public EventoResponseDTO updateEvento(
            @PathVariable UUID id,
            @RequestBody @Valid EventoDTO body,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        return this.evService.update(
                id,
                body,
                utenteLoggato
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void deleteEvento(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        this.evService.delete(
                id,
                utenteLoggato
        );
    }
}