package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.BadRequestException;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.repositories.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UtenteService {

    private final PasswordEncoder bcrypt;
    private UtenteRepository uRep;

    public UtenteService(UtenteRepository uRep, PasswordEncoder bcrypt) {
        this.uRep = uRep;
        this.bcrypt = bcrypt;
    }

    public Utente save(NuovoUtenteDTO body) {

        if (this.uRep.existsByEmail(body.email()))
            throw new BadRequestException("L'indirizzo email " + body.email() + " è già in uso!");

        Utente utenteSalvato = new Utente(body.nome(), body.cognome(), body.email(), this.bcrypt.encode(body.password()), body.ruolo());

        return this.uRep.save(utenteSalvato);

    }

    public Utente findByEmail(String email) {
        return uRep.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utente con email " + email + " non è stato trovato"));
    }

    public Utente findById(UUID id) {
        return uRep.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato"));
    }
}
