package fr.manu.petitesannonces.persistence.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BusinessException extends Exception implements SerializableCause {

	private static final long serialVersionUID = -7717192682422480242L;

    public static final String MESSAGE_KEY = "message";

    public static final String CAUSE_KEY = "cause";

	private final Map<String, String> parameters = new HashMap<String, String>();

	public BusinessException() {
		this((String) null, (SerializableCause) null);
	}

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

	public void removeParameter(final String name) {
		this.parameters.remove(name);
	}

	public String getParameter(final String name) {
		return this.parameters.get(name);
	}

	public boolean hasParameter(final String name) {
		return this.parameters.containsKey(name);
	}

	public Set<String> getParameterNames() {
		return this.parameters.keySet();
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}
}