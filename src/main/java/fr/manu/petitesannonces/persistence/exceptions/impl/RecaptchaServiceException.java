/**
 * 
 */
package fr.manu.petitesannonces.persistence.exceptions.impl;

import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author Manu
 *
 */
public class RecaptchaServiceException extends TechnicalException {

	private static final long serialVersionUID = -1347935575312298856L;

	/** Constructeur par défaut */
	public RecaptchaServiceException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public RecaptchaServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public RecaptchaServiceException(final String message) {
		super(message);
	}

	/**
	 * @param cause the cause
	 */
	public RecaptchaServiceException(final Throwable cause) {
		super(cause);
	}

}
