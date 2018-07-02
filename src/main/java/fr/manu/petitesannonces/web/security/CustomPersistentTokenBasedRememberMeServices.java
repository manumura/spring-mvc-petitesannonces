/**
 * 
 */
package fr.manu.petitesannonces.web.security;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import fr.manu.petitesannonces.persistence.services.PersistentTokenRepositoryService;

/**
 * @author Manu
 *
 */
public class CustomPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

	private PersistentTokenRepositoryService customTokenRepository;
	
	private UserDetailsService customUserDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomPersistentTokenBasedRememberMeServices.class);
	
	public CustomPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepositoryService tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		this.customTokenRepository = tokenRepository;
		this.customUserDetailsService = userDetailsService;
	}
	
	public UserDetails checkUserAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		
		logger.debug(">>>>> TokenValidator.checkUserAuthentication - start <<<<<");
		
        // TODO URI
		String uri = request.getScheme() 
				+ "://" 
				+ request.getServerName() 
				+ ":" 
				+ request.getServerPort() 
				+ request.getRequestURI();
		
		uri += (request.getQueryString() != null && !request.getQueryString().isEmpty()) ? "?" + request.getQueryString() : "";
		
		logger.debug(">>>>> TokenValidator.checkUserAuthentication - requested URI : " + uri + " <<<<<");
		
		final String rememberMeCookie = extractRememberMeCookie(request);
		
		if (rememberMeCookie == null) {
			logger.error("Cookie not found");
			throw new InvalidCookieException("Cookie not found");
		}

		logger.debug("Remember-me cookie detected");

		if (rememberMeCookie.length() == 0) {
			logger.error("Cookie was empty");
			cancelCookie(request, response);
			throw new InvalidCookieException("Cookie was empty");
		}

		final String[] cookieTokens = decodeCookie(rememberMeCookie);
		
		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain " + 2
					+ " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		final PersistentRememberMeToken token = customTokenRepository.getTokenForSeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
		}

		// We have a match for this user/series combination
		if (presentedToken == null || !presentedToken.equals(token.getTokenValue())) {
			// Token doesn't match series value. Delete all logins for this user and throw an exception to warn them.
			customTokenRepository.removeUserTokens(token.getUsername());
			throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
		}

		if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value, keeping the *same* series number.
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing persistent login token for user '"
					+ token.getUsername() + "', series '" + token.getSeries() + "'");
		}

		final PersistentRememberMeToken newToken = new PersistentRememberMeToken(
				token.getUsername(), token.getSeries(), generateTokenData(), new Date());

		try {
			customTokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(),
					newToken.getDate());
			setCookie(new String[] { newToken.getSeries(), newToken.getTokenValue() }, getTokenValiditySeconds(), request, response);
		}
		catch (Exception e) {
			logger.error("Failed to update token: ", e);
			throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
		}

        final UserDetails user = customUserDetailsService.loadUserByUsername(token.getUsername());
		
		logger.debug(">>>>> TokenValidator.checkUserAuthentication - end : " + user + " <<<<<");
		
		return user;
	}

}
