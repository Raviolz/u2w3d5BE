package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteResDTO;
import Raviolz.u2w3d5BE.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService uService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NuovoUtenteResDTO saveUtente(@RequestBody NuovoUtenteDTO body) {
        Utente saved = uService.save(body);
        return new NuovoUtenteResDTO(saved.getId());
    }
}


