/**
 * 
 */
package fr.manu.petitesannonces.web.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.web.constantes.Constantes;

/**
 * @author Manu
 *
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/*
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
		this.handle(request, response, authentication);
		this.clearAuthenticationAttributes(request);
	}

	/**
	 * Handle success
	 * 
	 * @param request the request
	 * @param response the response
	 * @param authentication the authentication
	 * @throws IOException the IOException
	 */
	private void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
			throws IOException {
		
		final String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/**
	 * Builds the target URL according to the logic defined in the main class.
	 * 
	 * @param authentication the authentication
	 * @return the target url
	 */
	private String determineTargetUrl(final Authentication authentication) {
		
		boolean isUser = false;
		boolean isAdmin = false;
		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		for (final GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals(Constantes.ROLE_USER)) {
				isUser = true;
			} else if (grantedAuthority.getAuthority().equals(Constantes.ROLE_ADMIN)) {
				isAdmin = true;
			}
		}

		if (isAdmin) {
            return RequestMappingUrl.ADMIN_LIST.getUrl();
		} else if (isUser) {
            return RequestMappingUrl.USER_EDIT.getUrl();
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * Clear authentication in session
	 * 
	 * @param request the request
	 */
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		
		final HttpSession session = request.getSession(false);
		
		if (session == null) {
			return;
		}
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
