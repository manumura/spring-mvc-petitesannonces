/**
 * 
 */
package fr.manu.petitesannonces.web.model;

import java.io.Serializable;

import fr.manu.petitesannonces.web.validator.PasswordMatches;
import fr.manu.petitesannonces.web.validator.ValidPassword;

/**
 * @author EmmanuelM
 *
 */
@PasswordMatches
public class PasswordModel implements Serializable {

    private static final long serialVersionUID = -8553687730986950044L;

    @ValidPassword(message = "{error.message.user.password.invalid}")
	private String password;

//    @ValidPassword(message = "{error.message.user.confirm.password.invalid}")
    private String confirmPassword;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PasswordModel [password=" + password + ", confirmPassword=" + confirmPassword + "]";
    }

}
