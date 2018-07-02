package fr.manu.petitesannonces.persistence.exceptions.impl;

import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import fr.manu.petitesannonces.persistence.exceptions.BusinessException;

/**
 * ApplicationBusinessException.java
 */
public class TokenValidationException extends BusinessException {

	/** serialVersionUID */
	private static final long serialVersionUID = -7496492316942772450L;
	
	/** Constructeur par défaut */
	public TokenValidationException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public TokenValidationException(final String message, final RememberMeAuthenticationException cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public TokenValidationException(final String message) {
		super(message);
	}

}
