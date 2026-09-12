package Raviolz.u2w3d5BE;

import Raviolz.u2w3d5BE.entities.Evento;
import Raviolz.u2w3d5BE.entities.Ruolo;
import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.EventoDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.payloads.PrenotazioneDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DomainValidationTests {

    @Autowired
    private Validator validator;

    @Test
    void eventoShouldUpdateCorrectly() {

        Utente organizzatore = new Utente(
                "Mario",
                "Rossi",
                "mario@test.it",
                "password",
                Ruolo.ORGANIZZATORE
        );

        Evento evento = new Evento(
                "Titolo originale",
                "Descrizione originale",
                LocalDate.now().plusDays(5),
                "Bergamo",
                50,
                organizzatore
        );

        LocalDate nuovaData = LocalDate.now().plusDays(10);

        evento.update(
                "Nuovo titolo",
                "Nuova descrizione",
                nuovaData,
                "Milano",
                80
        );

        assertEquals("Nuovo titolo", evento.getTitolo());
        assertEquals("Nuova descrizione", evento.getDescrizione());
        assertEquals(nuovaData, evento.getData());
        assertEquals("Milano", evento.getLuogo());
        assertEquals(80, evento.getPostiTotali());
    }

    @Test
    void utenteShouldUseEmailAsUsername() {

        Utente utente = new Utente(
                "Mario",
                "Rossi",
                "mario@test.it",
                "password",
                Ruolo.UTENTE
        );

        assertEquals(
                "mario@test.it",
                utente.getUsername()
        );
    }

    @Test
    void eventoWithPastDateShouldBeInvalid() {

        EventoDTO eventoDTO = new EventoDTO(
                "Concerto",
                "Descrizione evento",
                LocalDate.now().minusDays(1),
                "Bergamo",
                50
        );

        Set<ConstraintViolation<EventoDTO>> violations =
                validator.validate(eventoDTO);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("data")
                        )
        );
    }

    @Test
    void eventoWithZeroSeatsShouldBeInvalid() {

        EventoDTO eventoDTO = new EventoDTO(
                "Concerto",
                "Descrizione evento",
                LocalDate.now().plusDays(5),
                "Bergamo",
                0
        );

        Set<ConstraintViolation<EventoDTO>> violations =
                validator.validate(eventoDTO);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("postiTotali")
                        )
        );
    }

    @Test
    void eventoWithValidDataShouldHaveNoValidationErrors() {

        EventoDTO eventoDTO = new EventoDTO(
                "Concerto",
                "Descrizione evento",
                LocalDate.now().plusDays(5),
                "Bergamo",
                50
        );

        Set<ConstraintViolation<EventoDTO>> violations =
                validator.validate(eventoDTO);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void prenotazioneWithInvalidSeatsShouldBeRejected(
            int postiPrenotati
    ) {

        PrenotazioneDTO prenotazioneDTO =
                new PrenotazioneDTO(postiPrenotati);

        Set<ConstraintViolation<PrenotazioneDTO>> violations =
                validator.validate(prenotazioneDTO);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email",
            "email.com",
            "@",
            "test@",
            " "
    })
    void nuovoUtenteWithInvalidEmailShouldBeRejected(
            String invalidEmail
    ) {

        NuovoUtenteDTO utenteDTO = new NuovoUtenteDTO(
                "Mario",
                "Rossi",
                invalidEmail,
                "password123",
                Ruolo.UTENTE
        );

        Set<ConstraintViolation<NuovoUtenteDTO>> violations =
                validator.validate(utenteDTO);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("email")
                        )
        );
    }
}