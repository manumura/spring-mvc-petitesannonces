package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.SerializableCause;

/**
 * NotFoundException.java
 */
public class NotFoundException extends BusinessException {

	/** serialVersionUID */
	private static final long serialVersionUID = -7496492316942772450L;

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public NotFoundException(final String message, final SerializableCause cause) {
		super(message, cause);
	}
	
	/**
	 * Constructeur
	 * @param message the message
	 */
	public NotFoundException(final String message) {
		super(message);
	}

}
