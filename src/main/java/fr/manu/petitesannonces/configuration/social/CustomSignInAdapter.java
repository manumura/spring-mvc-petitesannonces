package fr.manu.petitesannonces.configuration.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;

/**
 * @author Manu
 *
 */
@Component
public class CustomSignInAdapter implements SignInAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    
    private static final Logger logger = LoggerFactory.getLogger(CustomSignInAdapter.class);

    @Override
    public String signIn(final String userId, final Connection<?> connection,
            final NativeWebRequest request) {

        logger.debug(">>>>> start - userId : {} <<<<<", userId);

        try {
            final User user = userService.get(Long.valueOf(userId));

            // Don't allow if email not confirmed
            if (user != null && user.getEmailConfirmed()) {
                final Authentication authentication = SecurityUtil.logInUser(user);

                // Set remember-me cookie
                persistentTokenBasedRememberMeServices.loginSuccess(
                        (HttpServletRequest) request.getNativeRequest(),
                        (HttpServletResponse) request.getNativeResponse(), authentication);
            }

            // Redirect to postSignInUrl
            return null;

        } catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> CustomSignInAdapter.signIn - Exception : {} <<<<<", e.getMessage(),
                    e);
            return null;
        }

    }
}