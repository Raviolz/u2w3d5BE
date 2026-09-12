package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Prenotazione;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.AlreadyExistException;
import Raviolz.u2w3d5BE.exception.BadRequestException;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.payloads.PrenotazioneDTO;
import Raviolz.u2w3d5BE.payloads.PrenotazioneResDTO;
import Raviolz.u2w3d5BE.repositories.PrenotazioneRepository;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository pRep;
    private final EventoService eventoService;

    public PrenotazioneService(
            PrenotazioneRepository pRep,
            EventoService eventoService
    ) {
        this.pRep = pRep;
        this.eventoService = eventoService;
    }

    public PrenotazioneResDTO save(
            UUID eventoId,
            PrenotazioneDTO body,
            Utente utenteLoggato
    ) {

        Evento evento = this.eventoService.findById(eventoId);

        if (evento.getData().isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Non puoi prenotare un evento già terminato"
            );
        }

        if (this.pRep.existsByUtenteIdAndEventoId(
                utenteLoggato.getId(),
                eventoId
        )) {
            throw new AlreadyExistException(
                    "Hai già una prenotazione per questo evento"
            );
        }

        int postiPrenotati = this.getPostiPrenotati(eventoId);
        int postiDisponibili = evento.getPostiTotali() - postiPrenotati;

        if (body.postiPrenotati() > postiDisponibili) {
            throw new BadRequestException(
                    "Posti insufficienti. Posti disponibili: " + postiDisponibili
            );
        }

        Prenotazione prenotazione = new Prenotazione(
                body.postiPrenotati(),
                utenteLoggato,
                evento
        );

        Prenotazione saved = this.pRep.save(prenotazione);

        return this.toResponseDTO(saved);
    }

    public List<PrenotazioneResDTO> findMyBookings(
            Utente utenteLoggato
    ) {
        return this.pRep
                .findByUtenteIdOrderByDataPrenotazioneDesc(
                        utenteLoggato.getId()
                )
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void delete(
            UUID id,
            Utente utenteLoggato
    ) {

        Prenotazione found = this.pRep.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Prenotazione con id " + id + " non trovata"
                        )
                );

        if (!found.getUtente()
                .getId()
                .equals(utenteLoggato.getId())) {

            throw new AuthorizationDeniedException(
                    "Puoi cancellare solo le tue prenotazioni"
            );
        }

        this.pRep.delete(found);
    }

    public int getPostiPrenotati(UUID eventoId) {
        Long totale = this.pRep.sumPostiPrenotatiByEventoId(eventoId);

        return totale.intValue();
    }

    public boolean hasBookings(UUID eventoId) {
        return this.pRep.existsByEventoId(eventoId);
    }

    private PrenotazioneResDTO toResponseDTO(
            Prenotazione prenotazione
    ) {

        Evento evento = prenotazione.getEvento();

        return new PrenotazioneResDTO(
                prenotazione.getId(),
                prenotazione.getPostiPrenotati(),
                prenotazione.getDataPrenotazione(),
                evento.getId(),
                evento.getTitolo(),
                evento.getData(),
                evento.getLuogo(),
                prenotazione.getUtente().getId()
        );
    }
}