/**
 *
 */
package fr.manu.petitesannonces.persistence.exceptions;

/**
 * Classe d'exception permettant de véhiculer des erreurs techniques.
 * <p>
 * Nota : Il est nécessaire de surclasser cette exception avec une adaptation
 * dédiée à un contexte d'erreur particulier.
 * </p>
 */
public abstract class TechnicalException extends Exception implements SerializableCause {

    /** serialVersionUID */
    private static final long serialVersionUID = -7717192682422480242L;

    /** Constructeur par défaut */
    public TechnicalException() {
        super();
    }

    /**
     * @param message the message
     * @param cause the cause
     */
    public TechnicalException(final String message, final Throwable cause) {
        super(message, SerializableThrowable.getCompatibleCause(cause));
    }

    /** @param message the message */
    public TechnicalException(final String message) {
        super(message);
    }

    /** @param cause the cause */
    public TechnicalException(final Throwable cause) {
        super(SerializableThrowable.getCompatibleCause(cause));
    }

    /*
      * @see java.lang.Throwable#initCause(java.lang.Throwable)
      */
    @Override
    public synchronized Throwable initCause(final Throwable cause) {
        return super.initCause(SerializableThrowable.getCompatibleCause(cause));
    }
}