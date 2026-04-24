package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.BadRequestException;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.exception.UnauthorizedException;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class EventoService {

    private final EventoRepository eRep;

    public EventoService(EventoRepository eRep) {
        this.eRep = eRep;
    }

    public Evento save(EventoDTO body, Utente organizzatore) {
        if (eRep.existsByTitoloAndDataAndLuogo(body.titolo(), body.data(), body.luogo())) {
            throw new BadRequestException("Evento già esistente");
        }
        Evento evento = new Evento(
                body.titolo(),
                body.descrizione(),
                body.data(),
                body.luogo(),
                body.postiTotali(),
                organizzatore
        );

        return eRep.save(evento);
    }


    public Evento findById(UUID id) {
        return eRep.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento con id " + id + " non trovato"));
    }

    public Evento update(UUID id, EventoDTO body, Utente utenteLoggato) {
        Evento found = this.findById(id);

        if (!found.getOrganizzatore().getId().equals(utenteLoggato.getId())) {
            throw new UnauthorizedException("Puoi modificare solo i tuoi eventi");
        }

        found.setTitolo(body.titolo());
        found.setDescrizione(body.descrizione());
        found.setData(body.data());
        found.setLuogo(body.luogo());
        found.setPostiTotali(body.postiTotali());

        return eRep.save(found);
    }

    public void delete(UUID id, Utente utenteLoggato) {
        Evento found = this.findById(id);

        if (!found.getOrganizzatore().getId().equals(utenteLoggato.getId())) {
            throw new UnauthorizedException("Non puoi eliminare gli eventi degli altri");
        }

        eRep.delete(found);
    }
}
