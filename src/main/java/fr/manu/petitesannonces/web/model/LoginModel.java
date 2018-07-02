/**
 * 
 */
package fr.manu.petitesannonces.web.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author EmmanuelM
 *
 */
public class LoginModel implements Serializable {

    private static final long serialVersionUID = -3403702575925880968L;

    @NotEmpty(message = "{error.message.user.login.not.empty}")
    @Size(min = 3, max = 20, message = "{error.message.user.login.size}")
	private String login;

    @NotEmpty(message = "{error.message.user.password.not.empty}")
    @Size(min = 8, max = 20, message = "{error.message.user.password.size}")
	private String password;

    // @NotEmpty
    // private Boolean acceptTerms;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoginModel [login=" + login + ", password=" + password + "]";
    }
	
}
