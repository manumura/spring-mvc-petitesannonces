package fr.manu.petitesannonces.web.exceptions;

import org.springframework.security.authentication.DisabledException;

import fr.manu.petitesannonces.dto.User;

/**
 * @author EmmanuelM
 *
 */
public class CustomDisabledException extends DisabledException {

	private static final long serialVersionUID = 2880607823817482016L;
	
	private final User user;

    public CustomDisabledException(final String msg, final User user) {
        super(msg);
        this.user = user;
    }

    public User getUser() {
		return user;
	}
}
