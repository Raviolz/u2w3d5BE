package Raviolz.u2w3d5BE.repositories;

import Raviolz.u2w3d5BE.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    Optional<Utente> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}