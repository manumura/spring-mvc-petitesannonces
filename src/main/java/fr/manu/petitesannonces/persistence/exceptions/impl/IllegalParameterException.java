package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.SerializableCause;

/**
 * ApplicationBusinessException.java
 */
public class IllegalParameterException extends BusinessException {

	/** serialVersionUID */
	private static final long serialVersionUID = -7496492316942772450L;
	
	/** Constructeur par défaut */
	public IllegalParameterException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public IllegalParameterException(final String message, final SerializableCause cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public IllegalParameterException(final String message) {
		super(message);
	}

}
