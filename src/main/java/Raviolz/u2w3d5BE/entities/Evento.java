package Raviolz.u2w3d5BE.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@NoArgsConstructor
@Getter
@Setter
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = false, length = 100)
    private String descrizione;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private String luogo;
    @Column(name = "posti_totali", nullable = false)
    private int postiTotali;
    @ManyToOne
    @JoinColumn(name = "id_organizzatore", nullable = false)
    private Utente organizzatore;

    public Evento(String titolo, String descrizione, LocalDate data, String luogo, int postiTotali, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.postiTotali = postiTotali;
        this.organizzatore = organizzatore;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", data=" + data +
                ", luogo='" + luogo + '\'' +
                ", postiTotali=" + postiTotali +
                ", organizzatoreId=" + organizzatore.getId() +
                '}';
    }
}