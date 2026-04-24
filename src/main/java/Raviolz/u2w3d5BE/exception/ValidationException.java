package Raviolz.u2w3d5BE.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errors;

    public ValidationException(List<String> errors) {
        super("Ci sono errori di validazione");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}