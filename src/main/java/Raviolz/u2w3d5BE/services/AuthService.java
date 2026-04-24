package Raviolz.u2w3d5BE.services;

import Raviolz.u2w3d5BE.entities.Utente;
import Raviolz.u2w3d5BE.exception.NotFoundException;
import Raviolz.u2w3d5BE.exception.UnauthorizedException;
import Raviolz.u2w3d5BE.payloads.LoginDTO;
import Raviolz.u2w3d5BE.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteService uService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UtenteService uService, TokenTools tokenTools, PasswordEncoder bcrypt) {
        this.uService = uService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {

        try {
            Utente found = this.uService.findByEmail(body.email());

            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}