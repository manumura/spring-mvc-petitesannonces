/**
 * 
 */
package fr.manu.petitesannonces.dto;

import java.util.ArrayList;
import java.util.List;

import fr.manu.petitesannonces.dto.enums.UserRoleType;

/**
 * @author EmmanuelM
 *
 */
public class UserPrincipal {

	private String username;
	
	private List<UserRoleType> roles = new ArrayList<UserRoleType>();

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the roles
	 */
	public List<UserRoleType> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<UserRoleType> roles) {
		this.roles = roles;
	}

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserPrincipal [username=" + username + ", roles=" + roles + "]";
    }

}
