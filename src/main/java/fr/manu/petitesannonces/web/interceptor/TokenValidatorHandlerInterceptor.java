/**
 * 
 */
package fr.manu.petitesannonces.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import fr.manu.petitesannonces.persistence.exceptions.impl.TokenValidationException;
import fr.manu.petitesannonces.persistence.services.PersistentTokenRepositoryService;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.security.CustomPersistentTokenBasedRememberMeServices;

/**
 * @author Manu
 *
 */
@Component
public class TokenValidatorHandlerInterceptor implements HandlerInterceptor {
	
	@Autowired
	@Qualifier("customPersistentTokenRepository")
	private PersistentTokenRepositoryService tokenRepository;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(TokenValidatorHandlerInterceptor.class);

	@Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
		
		//TODO check passages ici
		logger.debug(">>>>> CustomHandlerInterceptor.preHandle - start <<<<<");
		
		final CustomPersistentTokenBasedRememberMeServices tokenValidator = new CustomPersistentTokenBasedRememberMeServices(Constantes.REMEMBER_ME_KEY, userDetailsService, tokenRepository);
		
		try {
			final UserDetails user = tokenValidator.checkUserAuthentication(request, response);
			final boolean result = (user != null && user.isAccountNonExpired());
	        
	        if (!result) {
	        	logger.error(">>>>> CustomHandlerInterceptor.preHandle - token validation error <<<<<");
	        	response.sendRedirect(request.getContextPath() + "/logout");
	        }
	        
	        logger.debug(">>>>> CustomHandlerInterceptor.preHandle - end : " + user + " <<<<<");
	        
	        // TODO : update user last action
	        
	        return result;
			
		} catch (InvalidCookieException ice) {
			logger.error(">>>>> CustomHandlerInterceptor.preHandle - InvalidCookieException token validation error : {} <<<<<", ice.getMessage());
			throw new TokenValidationException(">>>>> CustomHandlerInterceptor.preHandle - InvalidCookieException token validation error : " + ice.getMessage() + " <<<<<", ice);
			
		} catch (CookieTheftException cte) {
			logger.error(">>>>> CustomHandlerInterceptor.preHandle - CookieTheftException token validation error : {} <<<<<", cte.getMessage());
			throw new TokenValidationException(">>>>> CustomHandlerInterceptor.preHandle - CookieTheftException token validation error : " + cte.getMessage() + " <<<<<", cte);
			
		} catch (RememberMeAuthenticationException rmae) {
			logger.error(">>>>> CustomHandlerInterceptor.preHandle - RememberMeAuthenticationException token validation error : {} <<<<<", rmae.getMessage());
			throw new TokenValidationException(">>>>> CustomHandlerInterceptor.preHandle - RememberMeAuthenticationException token validation error : " + rmae.getMessage() + " <<<<<", rmae);
		}
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
    		final ModelAndView modelAndView) throws Exception {
    	// nothing to do here
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
    		final Exception ex) throws Exception {
    	// nothing to do here
    }

}
