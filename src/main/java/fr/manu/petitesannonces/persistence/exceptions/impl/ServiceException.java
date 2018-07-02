package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * ServiceException
 */
public class ServiceException extends TechnicalException {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6600948062826145550L;

	/** Constructeur par défaut */
	public ServiceException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public ServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public ServiceException(final String message) {
		super(message);
	}

	/**
	 * @param cause the cause
	 */
	public ServiceException(final Throwable cause) {
		super(cause);
	}

}
