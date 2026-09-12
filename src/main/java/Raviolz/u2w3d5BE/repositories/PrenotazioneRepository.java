package Raviolz.u2w3d5BE.repositories;

import Raviolz.u2w3d5BE.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    boolean existsByUtenteIdAndEventoId(UUID utenteId, UUID eventoId);

    boolean existsByEventoId(UUID eventoId);

    List<Prenotazione> findByUtenteIdOrderByDataPrenotazioneDesc(UUID utenteId);

    @Query("""
            SELECT SUM(p.postiPrenotati)
            FROM Prenotazione p
            WHERE p.evento.id = :eventoId
            """)
    Long sumPostiPrenotatiByEventoId(@Param("eventoId") UUID eventoId);
}