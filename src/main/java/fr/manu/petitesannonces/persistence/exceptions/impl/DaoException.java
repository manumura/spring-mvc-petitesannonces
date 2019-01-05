package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * DaoException
 */
public class DaoException extends TechnicalException {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6600948062826145550L;

	public DaoException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public DaoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public DaoException(final String message) {
		super(message);
	}

	/**
	 * @param cause the cause
	 */
	public DaoException(final Throwable cause) {
		super(cause);
	}

}
