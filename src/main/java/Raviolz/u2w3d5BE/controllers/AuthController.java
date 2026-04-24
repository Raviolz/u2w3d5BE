package Raviolz.u2w3d5BE.controllers;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.ValidationException;
import Raviolz.u2w3d5BE.payloads.LoginDTO;
import Raviolz.u2w3d5BE.payloads.LoginResDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteDTO;
import Raviolz.u2w3d5BE.payloads.NuovoUtenteResDTO;
import Raviolz.u2w3d5BE.services.AuthService;
import Raviolz.u2w3d5BE.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService uService;


    public AuthController(AuthService authService, UtenteService uService) {
        this.authService = authService;
        this.uService = uService;
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody LoginDTO body) {
        return new LoginResDTO(this.authService.checkCredentialAndGenerateToken(body));
    }


    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED) //201
    public NuovoUtenteResDTO saveUser(@RequestBody @Validated NuovoUtenteDTO body, BindingResult validationResult) {
//
//
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            throw new ValidationException(errors);
        }
//
        Utente u = this.uService.save(body);
        return new NuovoUtenteResDTO(u.getId());
    }
}