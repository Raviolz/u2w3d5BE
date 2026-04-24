package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.AlreadyExistException;
import Raviolz.u2w3d5BE.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository uRep;

    public Utente save(Utente utente) {

        if (uRep.findByEmail(utente.getEmail()).isPresent()) {
            throw new AlreadyExistException("Utente gia' registrato con questa mail");
        }

        return uRep.save(utente);
    }
}
