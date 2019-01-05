package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.SerializableCause;

/**
 * ApplicationBusinessException.java
 */
public class ApplicationBusinessException extends BusinessException {

	/** serialVersionUID */
	private static final long serialVersionUID = -7496492316942772450L;
	
	public ApplicationBusinessException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public ApplicationBusinessException(final String message, final SerializableCause cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public ApplicationBusinessException(final String message) {
		super(message);
	}

}
