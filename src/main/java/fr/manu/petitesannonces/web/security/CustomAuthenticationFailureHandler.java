/**
 * 
 */
package fr.manu.petitesannonces.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.web.constantes.Constantes;

/**
 * @author Manu
 *
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

	@Override
    public void onAuthenticationFailure(final HttpServletRequest request,
            final HttpServletResponse response, final AuthenticationException exception)
			throws IOException, ServletException {

        logger.error(">>>>> Authentication failed : " + exception.getMessage() + " <<<<<",
                exception);
		
        setDefaultFailureUrl(RequestMappingUrl.LOGIN_FAILURE.getUrl());
        super.onAuthenticationFailure(request, response, exception);

        // Add authentication exception to session
        request.getSession().setAttribute(Constantes.AUTHENTICATION_USER_LOGIN,
                request.getParameter(Constantes.LOGIN_PARAMETER));
        request.getSession().setAttribute(Constantes.AUTHENTICATION_LAST_EXCEPTION, exception);
	}

}
