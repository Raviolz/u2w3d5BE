package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.AlreadyExistException;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.repositories.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {

    private final UtenteRepository uRep;
    private final PasswordEncoder bcrypt;

    public UtenteService(UtenteRepository uRep, PasswordEncoder bcrypt) {
        this.uRep = uRep;
        this.bcrypt = bcrypt;
    }

    public Utente save(NuovoUtenteDTO body) {

        String email = body.email().trim().toLowerCase();

        if (this.uRep.existsByEmailIgnoreCase(email)) {
            throw new AlreadyExistException(
                    "L'indirizzo email " + email + " è già in uso"
            );
        }

        Utente nuovoUtente = new Utente(
                body.nome().trim(),
                body.cognome().trim(),
                email,
                this.bcrypt.encode(body.password()),
                body.ruolo()
        );

        return this.uRep.save(nuovoUtente);
    }

    public Utente findByEmail(String email) {
        return this.uRep.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Utente con email " + email + " non trovato"
                        )
                );
    }

    public Utente findById(UUID id) {
        return this.uRep.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Utente con id " + id + " non trovato"
                        )
                );
    }
}