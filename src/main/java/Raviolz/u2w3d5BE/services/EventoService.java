package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.AlreadyExistException;
import Raviolz.u2w3d5BE.exception.BadRequestException;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.payloads.EventoResponseDTO;
import Raviolz.u2w3d5BE.repositories.EventoRepository;
import Raviolz.u2w3d5BE.repositories.PrenotazioneRepository;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eRep;
    private final PrenotazioneRepository pRep;

    public EventoService(
            EventoRepository eRep,
            PrenotazioneRepository pRep
    ) {
        this.eRep = eRep;
        this.pRep = pRep;
    }

    public EventoResponseDTO save(
            EventoDTO body,
            Utente organizzatore
    ) {

        String titolo = body.titolo().trim();
        String descrizione = body.descrizione().trim();
        String luogo = body.luogo().trim();

        if (this.eRep.existsByTitoloIgnoreCaseAndDataAndLuogoIgnoreCase(
                titolo,
                body.data(),
                luogo
        )) {
            throw new AlreadyExistException(
                    "Esiste già un evento con lo stesso titolo, data e luogo"
            );
        }

        Evento evento = new Evento(
                titolo,
                descrizione,
                body.data(),
                luogo,
                body.postiTotali(),
                organizzatore
        );

        Evento saved = this.eRep.save(evento);

        return this.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> findAll() {
        return this.eRep.findAllByOrderByDataAsc()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO findByIdResponse(UUID id) {
        return this.toResponseDTO(this.findById(id));
    }

    public Evento findById(UUID id) {
        return this.eRep.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento con id " + id + " non trovato"
                        )
                );
    }

    public EventoResponseDTO update(
            UUID id,
            EventoDTO body,
            Utente utenteLoggato
    ) {

        Evento found = this.findById(id);

        // Controllo ownership:
        // l'organizzatore può modificare solo i propri eventi
        if (!found.getOrganizzatore()
                .getId()
                .equals(utenteLoggato.getId())) {

            throw new AuthorizationDeniedException(
                    "Puoi modificare solo i tuoi eventi"
            );
        }

        String titolo = body.titolo().trim();
        String descrizione = body.descrizione().trim();
        String luogo = body.luogo().trim();

        // Controllo che non esista un ALTRO evento
        // con stesso titolo, data e luogo
        if (this.eRep
                .existsByTitoloIgnoreCaseAndDataAndLuogoIgnoreCaseAndIdNot(
                        titolo,
                        body.data(),
                        luogo,
                        id
                )) {

            throw new AlreadyExistException(
                    "Esiste già un altro evento con lo stesso titolo, data e luogo"
            );
        }

        // Calcoliamo quanti posti sono già stati prenotati
        Long totalePrenotato =
                this.pRep.sumPostiPrenotatiByEventoId(id);

        int postiGiaPrenotati =
                totalePrenotato == null
                        ? 0
                        : totalePrenotato.intValue();

        // Se esistono già 30 posti prenotati,
        // l'organizzatore non può ridurre i posti totali a 20
        if (body.postiTotali() < postiGiaPrenotati) {
            throw new BadRequestException(
                    "Non puoi ridurre i posti totali sotto il numero di posti già prenotati: "
                            + postiGiaPrenotati
            );
        }

        found.update(
                titolo,
                descrizione,
                body.data(),
                luogo,
                body.postiTotali()
        );

        Evento updated = this.eRep.save(found);

        return this.toResponseDTO(updated);
    }

    public void delete(
            UUID id,
            Utente utenteLoggato
    ) {

        Evento found = this.findById(id);

        // Controllo ownership
        if (!found.getOrganizzatore()
                .getId()
                .equals(utenteLoggato.getId())) {

            throw new AuthorizationDeniedException(
                    "Puoi eliminare solo i tuoi eventi"
            );
        }

        // Non permettiamo di cancellare un evento
        // che ha già delle prenotazioni
        if (this.pRep.existsByEventoId(id)) {
            throw new BadRequestException(
                    "Non puoi eliminare un evento con prenotazioni attive"
            );
        }

        this.eRep.delete(found);
    }

    private EventoResponseDTO toResponseDTO(Evento evento) {

        Utente organizzatore = evento.getOrganizzatore();

        Long totalePrenotato =
                this.pRep.sumPostiPrenotatiByEventoId(evento.getId());

        int postiPrenotati =
                totalePrenotato == null
                        ? 0
                        : totalePrenotato.intValue();

        int postiDisponibili =
                evento.getPostiTotali() - postiPrenotati;

        return new EventoResponseDTO(
                evento.getId(),
                evento.getTitolo(),
                evento.getDescrizione(),
                evento.getData(),
                evento.getLuogo(),
                evento.getPostiTotali(),
                postiDisponibili,
                organizzatore.getId(),
                organizzatore.getNome(),
                organizzatore.getCognome()
        );
    }
}