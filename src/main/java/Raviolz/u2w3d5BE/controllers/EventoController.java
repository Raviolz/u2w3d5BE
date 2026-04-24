package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.ValidationException;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.services.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    private final EventoService evService;

    public EventoController(EventoService eventoService) {
        this.evService = eventoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento saveEvento(
            @RequestBody @Validated EventoDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            throw new ValidationException(errors);
        }

        return evService.save(body, utenteLoggato);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento updateEvento(
            @PathVariable UUID id,
            @RequestBody @Validated EventoDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            throw new ValidationException(errors);
        }

        return evService.update(id, body, utenteLoggato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void deleteEvento(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utenteLoggato
    ) {
        evService.delete(id, utenteLoggato);
    }


}
