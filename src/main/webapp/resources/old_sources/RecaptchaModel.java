package fr.manu.petitesannonces.web.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Manu
 *
 */
public abstract class RecaptchaModel implements Serializable {

	private static final long serialVersionUID = -4709775459884433611L;
	
    @NotEmpty(message = "{error.message.recaptcha.not.empty}")
    private String recaptchaResponse;

	/**
	 * @return the recaptchaResponse
	 */
	public String getRecaptchaResponse() {
		return recaptchaResponse;
	}

	/**
	 * @param recaptchaResponse the recaptchaResponse to set
	 */
	public void setRecaptchaResponse(String recaptchaResponse) {
		this.recaptchaResponse = recaptchaResponse;
	}
	
}
