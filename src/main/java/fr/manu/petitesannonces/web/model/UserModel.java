/**
 * 
 */
package fr.manu.petitesannonces.web.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Manu
 *
 */
public class UserModel extends UserBasicInformationModel implements Serializable {

	private static final long serialVersionUID = -536100632384391231L;
	
    @NotEmpty(message = "{error.message.user.login.not.empty}")
    @Size(min = 3, max = 20, message = "{error.message.user.login.size}")
	private String login;

    @NotEmpty(message = "{error.message.user.password.not.empty}")
    @Size(min = 8, max = 20, message = "{error.message.user.password.size}")
	private String password;

    @NotEmpty(message = "{error.message.user.confirm.password.not.empty}")
    @Size(min = 8, max = 20, message = "{error.message.user.confirm.password.size}")
	private String confirmPassword;

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
        return "UserModel [login=" + login + ", password=" + password
                + ", confirmPassword=" + confirmPassword
				+ ", toString()=" + super.toString() + "]";
	}

}