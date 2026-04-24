package Raviolz.u2w3d5BE.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@NoArgsConstructor
@Getter
@Setter
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(name = "posti_prenotati", nullable = false)
    private int postiPrenotati;
    @Column(name = "data_prenotazione", nullable = false)
    private LocalDate dataPrenotazione;
    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    public Prenotazione(int postiPrenotati, Utente utente, Evento evento) {
        this.postiPrenotati = postiPrenotati;
        this.dataPrenotazione = LocalDate.now();
        this.utente = utente;
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", posti prenotati=" + postiPrenotati +
                ", data prenotazione=" + dataPrenotazione +
                ", utenteId=" + utente.getId() +
                ", eventoId=" + evento.getId() +
                '}';
    }
}