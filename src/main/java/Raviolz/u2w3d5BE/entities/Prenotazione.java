package Raviolz.u2w3d5BE.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "prenotazioni",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prenotazione_utente_evento",
                        columnNames = {"id_utente", "id_evento"}
                )
        }
)
@NoArgsConstructor
@Getter
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "posti_prenotati", nullable = false)
    private int postiPrenotati;

    @Column(
            name = "data_prenotazione",
            nullable = false,
            updatable = false
    )
    private LocalDateTime dataPrenotazione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    public Prenotazione(int postiPrenotati, Utente utente, Evento evento) {
        this.postiPrenotati = postiPrenotati;
        this.utente = utente;
        this.evento = evento;
    }

    @PrePersist
    public void setDataPrenotazione() {
        this.dataPrenotazione = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", postiPrenotati=" + postiPrenotati +
                ", dataPrenotazione=" + dataPrenotazione +
                '}';
    }
}