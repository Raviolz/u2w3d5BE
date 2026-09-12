package Raviolz.u2w3d5BE.repositories;

import Raviolz.u2w3d5BE.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {

    boolean existsByTitoloIgnoreCaseAndDataAndLuogoIgnoreCase(
            String titolo,
            LocalDate data,
            String luogo
    );

    boolean existsByTitoloIgnoreCaseAndDataAndLuogoIgnoreCaseAndIdNot(
            String titolo,
            LocalDate data,
            String luogo,
            UUID id
    );

    List<Evento> findAllByOrderByDataAsc();
}