/**
 *
 */
package fr.manu.petitesannonces.persistence.exceptions;

public class SerializableThrowable extends Throwable implements SerializableCause {

    private static final long serialVersionUID = -4757122414659315913L;

    private final String originalName;

    private final String originalMessage;

    public SerializableThrowable(final Throwable source) {
        super(source.getMessage(), getCompatibleCause(source.getCause()));

        // copie des informations
        this.originalName = source.getClass().getName();
        this.originalMessage = source.getLocalizedMessage();
        this.setStackTrace(source.getStackTrace());
    }

    public static final Throwable getCompatibleCause(final Throwable t) {
        final Throwable res;

        if (t == null) {
            res = null;
        } else if (t instanceof SerializableCause) {
            res = t;
        } else {
            res = new SerializableThrowable(t);
        }

        return res;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Throwable#fillInStackTrace()
      */
    @Override
    public synchronized Throwable fillInStackTrace() {
        // rien � faire ici (emp�che la mise � jour de la pile d'appel
        return this;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Throwable#initCause(java.lang.Throwable)
      */
    @Override
    public synchronized Throwable initCause(final Throwable cause) {
        return super.initCause(getCompatibleCause(cause));
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Throwable#getLocalizedMessage()
      */
    @Override
    public String getLocalizedMessage() {
        return this.originalMessage;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Throwable#toString()
      */
    @Override
    public String toString() {
        return String.format("%s: %s", this.originalName, this.getMessage());
    }
}
