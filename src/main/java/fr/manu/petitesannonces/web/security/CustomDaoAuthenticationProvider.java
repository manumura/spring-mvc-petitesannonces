/**
 * 
 */
package fr.manu.petitesannonces.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.web.exceptions.CustomDisabledException;
import fr.manu.petitesannonces.web.exceptions.LoginModelValidationException;
import fr.manu.petitesannonces.web.model.LoginModel;
import fr.manu.petitesannonces.web.validator.LoginModelValidator;

/**
 * @author EmmanuelM
 *
 */
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

    @Autowired
    private LoginModelValidator loginModelValidator;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(CustomDaoAuthenticationProvider.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#
     * authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate(final Authentication authentication)
            throws AuthenticationException {

        logger.debug(">>>>> CustomDaoAuthenticationProvider.authenticate - start <<<<<");

        final LoginModel loginModel = new LoginModel();
        loginModel.setLogin(authentication.getName());
        loginModel.setPassword(authentication.getCredentials().toString());

        final BindingResult bindingResult = new BeanPropertyBindingResult(loginModel, "loginModel");
        loginModelValidator.validate(loginModel, bindingResult);

        if (bindingResult.hasErrors()) {

            // Sort by keys
            final Map<String, String> errorMap = new TreeMap<String, String>();
            final List<String> errorMessages = new ArrayList<String>(0);

            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.debug(">>>>> Field error : {} <<<<<", error.getDefaultMessage());

                final String errorMessage =
                        messageSource.getMessage(error, LocaleContextHolder.getLocale());
                logger.debug(">>>>> Error message : {} <<<<<", errorMessage);

                if (error.getField() != null && !error.getField().isEmpty() && errorMessage != null
                        && !errorMessage.isEmpty()) {
                    errorMap.put(error.getField(), errorMessage);
                }

                // modelAndView.addObject("errorMessage", error.getDefaultMessage());
            }

            // Get messages sorted by field name
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                errorMessages.add(entry.getValue());
            }

            for (ObjectError error : bindingResult.getGlobalErrors()) {
                logger.debug(">>>>> Object error : {} <<<<<", error.getDefaultMessage());

                final String errorMessage =
                        messageSource.getMessage(error, LocaleContextHolder.getLocale());
                logger.debug(">>>>> Error message : {} <<<<<", errorMessage);

                if (errorMessage != null && !errorMessage.isEmpty()) {
                    errorMessages.add(errorMessage);
                }

                // modelAndView.addObject("errorMessage", error.getDefaultMessage());
            }

            throw new LoginModelValidationException("Login model validation failed", bindingResult,
                    loginModel, errorMessages);
        }
        
        String username = (String) authentication.getPrincipal();
        if (!StringUtils.isEmpty(username)) {
        	try {
	            User user = userService.getByLogin(username);
	            if (user != null && !user.getEmailConfirmed()) {
	            	logger.error(">>>>> User [{}] : email not confirmed <<<<<", user.getLogin());
	                throw new CustomDisabledException(">>>>> User [ " + user.getLogin() + "] : email not confirmed <<<<<", user);
	            }
	            
        	} catch (BusinessException | TechnicalException e) {
                logger.error(">>>>> Exception in CustomDaoAuthenticationProvider.authenticate : {} <<<<<", e.getMessage(), e);
            }
        }
        
        final Authentication authenticationResult = super.authenticate(authentication);
        logger.debug(">>>>> CustomDaoAuthenticationProvider.authenticate - end : {} <<<<<", authenticationResult);

        return authenticationResult;
    }
}
