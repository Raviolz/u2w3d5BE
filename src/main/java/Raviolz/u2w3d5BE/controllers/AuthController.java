package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.payloads.LoginDTO;
import Raviolz.u2w3d5BE.payloads.LoginResDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteResDTO;
import Raviolz.u2w3d5BE.services.AuthService;
import Raviolz.u2w3d5BE.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService uService;

    public AuthController(
            AuthService authService,
            UtenteService uService
    ) {
        this.authService = authService;
        this.uService = uService;
    }

    @PostMapping("/login")
    public LoginResDTO login(
            @RequestBody @Valid LoginDTO body
    ) {
        String token = this.authService.checkCredentialAndGenerateToken(body);

        return new LoginResDTO(token);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public NuovoUtenteResDTO registration(
            @RequestBody @Valid NuovoUtenteDTO body
    ) {
        Utente saved = this.uService.save(body);

        return new NuovoUtenteResDTO(saved.getId());
    }
}