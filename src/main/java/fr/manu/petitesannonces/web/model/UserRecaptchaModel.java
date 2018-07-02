/**
 * 
 */
package fr.manu.petitesannonces.web.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Manu
 *
 */
public class UserRecaptchaModel extends UserModel {

	private static final long serialVersionUID = -8033959163599590991L;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserRecaptchaModel [recaptchaResponse=" + recaptchaResponse + ", toString()=" + super.toString() + "]";
	}
	
}
