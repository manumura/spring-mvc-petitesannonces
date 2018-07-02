/**
 * 
 */
package fr.manu.petitesannonces.configuration.social;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import fr.manu.petitesannonces.dto.SocialUser;
import fr.manu.petitesannonces.dto.User;

/**
 * @author Manu
 *
 */
public class SecurityUtil {

	// Auto login.
	public static Authentication logInUser(final User user) {
		final UserDetails userDetails = new SocialUser(user);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}
}
