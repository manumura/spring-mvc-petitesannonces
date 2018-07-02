/**
 * 
 */
package fr.manu.petitesannonces.configuration.social;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.security.SocialAuthenticationFailureHandler;
import org.springframework.social.security.SocialAuthenticationRedirectException;

import fr.manu.petitesannonces.web.constantes.Constantes;

/**
 * @author Manu
 *
 */
public class CutomSocialAuthenticationFailureHandler extends SocialAuthenticationFailureHandler {

	public CutomSocialAuthenticationFailureHandler(AuthenticationFailureHandler delegate) {
		super(delegate);
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		if (failed instanceof SocialAuthenticationRedirectException) {
			response.sendRedirect(((SocialAuthenticationRedirectException) failed).getRedirectUrl()); 
			return;
		}
		getDelegate().onAuthenticationFailure(request, response, failed);
		
		// Add authentication exception to session
        request.getSession().setAttribute(Constantes.AUTHENTICATION_USER_LOGIN,
                request.getParameter(Constantes.LOGIN_PARAMETER));
        request.getSession().setAttribute(Constantes.AUTHENTICATION_LAST_EXCEPTION, failed);
	}
	
}
