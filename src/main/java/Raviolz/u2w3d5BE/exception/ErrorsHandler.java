package Raviolz.u2w3d5BE.exception;

import Raviolz.u2w3d5BE.payloads.ErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleValidation(
            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField()
                                + ": "
                                + error.getDefaultMessage()
                )
                .distinct()
                .collect(Collectors.joining("; "));

        return new ErrorsDTO(
                message,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleUnreadableBody(
            HttpMessageNotReadableException ex
    ) {
        return new ErrorsDTO(
                "Corpo della richiesta non valido",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorsDTO handleAlreadyExist(
            AlreadyExistException ex
    ) {
        return new ErrorsDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(
            NotFoundException ex
    ) {
        return new ErrorsDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest(
            BadRequestException ex
    ) {
        return new ErrorsDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorized(
            UnauthorizedException ex
    ) {
        return new ErrorsDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsDTO handleForbidden(
            AuthorizationDeniedException ex
    ) {
        return new ErrorsDTO(
                "Non hai i permessi per eseguire questa operazione",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGeneric(
            Exception ex
    ) {
        ex.printStackTrace();

        return new ErrorsDTO(
                "Errore lato server",
                LocalDateTime.now()
        );
    }
}