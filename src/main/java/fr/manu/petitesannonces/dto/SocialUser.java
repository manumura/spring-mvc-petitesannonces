/**
 * 
 */
package fr.manu.petitesannonces.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import fr.manu.petitesannonces.web.constantes.Constantes;

/**
 * @author Manu
 *
 */
// TODO : a voir pour completer
public class SocialUser implements SocialUserDetails {
	
	private static final long serialVersionUID = -8552819325947096064L;
	
	private User user;
	
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	
    public SocialUser(final User user) {
        this.user = user;
        
        for (final UserRole role : this.user.getUserRoles()) {
        	final GrantedAuthority authority = new SimpleGrantedAuthority(Constantes.ROLE_PREFIX + role.getType());
            this.authorities.add(authority);
        }
    }

	@Override
	public String getUserId() {
		return this.user.getId().toString();
	}
	
	@Override
	public String getUsername() {
		return this.user.getLogin();
	}
	
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
        return this.user.getEmailConfirmed();
	}

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

}
