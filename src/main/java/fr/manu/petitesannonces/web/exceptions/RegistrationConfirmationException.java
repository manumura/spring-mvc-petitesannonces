package fr.manu.petitesannonces.web.exceptions;

/**
 * @author emmanuel.mura
 *
 */
public class RegistrationConfirmationException extends Exception {

    private static final long serialVersionUID = -5309982202390054697L;

    public RegistrationConfirmationException() {
    }

    public RegistrationConfirmationException(String message) {
        super(message);
    }

    public RegistrationConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }
}
