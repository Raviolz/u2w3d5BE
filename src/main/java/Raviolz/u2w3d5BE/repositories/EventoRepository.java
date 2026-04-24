package Raviolz.u2w3d5BE.repositories;

import Raviolz.u2w3d5BE.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
