package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.BadRequestException;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.repositories.EventoRepository;
import org.springframework.stereotype.Service;


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
}
