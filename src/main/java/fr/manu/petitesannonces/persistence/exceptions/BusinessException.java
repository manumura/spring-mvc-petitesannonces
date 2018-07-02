package fr.manu.petitesannonces.persistence.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe d'exception permettant de v�hiculer des erreurs m�tier.
 * <p>Nota : Il est n�cessaire de surclasser cette exception avec une adaptation d�di�e � un contexte d'erreur particulier.</p>
 */
public abstract class BusinessException extends Exception implements SerializableCause {

	/** serialVersionUID. */
	private static final long serialVersionUID = -7717192682422480242L;

	/** Clef de message. */
    public static final String MESSAGE_KEY = "message";

	/** Clef de cause. */
    public static final String CAUSE_KEY = "cause";

	/** Paires clef/valeur de param�trage. */
	private final Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Constructeur par d�faut (pour la s�rialisation).
	 */
	public BusinessException() {
		this((String) null, (SerializableCause) null);
	}

	/**
	 * Constructeur prot�g� (traitement des exceptions).
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	protected BusinessException(final String message, final SerializableCause cause) {
		super(message, (Throwable) cause);
		addParameter(BusinessException.MESSAGE_KEY, message);

		if (cause != null) {
			addParameter(BusinessException.CAUSE_KEY, cause.toString());
		}
	}
	
	/**
     * @param message the message
     * @param cause the cause
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, SerializableThrowable.getCompatibleCause(cause));
    }

    /** @param cause the cause */
    public BusinessException(final Throwable cause) {
        super(SerializableThrowable.getCompatibleCause(cause));
    }

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public BusinessException(final String message, final BusinessException cause) {
		this(message, (SerializableCause) cause);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public BusinessException(final String message, final TechnicalException cause) {
		this(message, (SerializableCause) cause);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param message
	 *            the message
	 */
	public BusinessException(final String message) {
		this(message, (SerializableCause) null);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public BusinessException(final BusinessException cause) {
		this(null, (SerializableCause) cause);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public BusinessException(final TechnicalException cause) {
		this(null, (SerializableCause) cause);
	}

	/**
	 * Ajoute un param�tre.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addParameter(final String name, final String value) {
		if (name != null && !name.isEmpty()) {
			if (value == null || value.isEmpty()) {
				this.parameters.remove(name);
			}
			else {
				this.parameters.put(name, value);
			}
		}
	}

	/**
	 * Supprime un param�tre.
	 * 
	 * @param name
	 *            the name
	 */
	public void removeParameter(final String name) {
		this.parameters.remove(name);
	}

	/**
	 * R�cup�re un param�tre.
	 * 
	 * @param name
	 *            the name
	 * @return the parameter
	 */
	public String getParameter(final String name) {
		return this.parameters.get(name);
	}

	/**
	 * D�termine si un param�tre est pr�sent.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean hasParameter(final String name) {
		return this.parameters.containsKey(name);
	}

	/**
	 * Retourne la liste des param�tres.
	 * 
	 * @return the parameter names
	 */
	public Set<String> getParameterNames() {
		return this.parameters.keySet();
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}
}