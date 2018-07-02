/**
 * 
 */
package fr.manu.petitesannonces.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

import fr.manu.petitesannonces.dto.enums.Page;
import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.persistence.exceptions.impl.ApplicationBusinessException;
import fr.manu.petitesannonces.persistence.exceptions.impl.ServiceException;
import fr.manu.petitesannonces.persistence.exceptions.impl.TokenValidationException;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.constantes.ModelAttributesConstantes;
import fr.manu.petitesannonces.web.exceptions.CustomDisabledException;
import fr.manu.petitesannonces.web.exceptions.LoginModelValidationException;
import fr.manu.petitesannonces.web.model.LoginModel;

/**
 * @author Manu
 *
 */
@ControllerAdvice
public class GlobalExceptionController {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final BadCredentialsException ex,
            final Locale locale) {

        logger.debug(">>>>> BadCredentialsException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage =
                messageSource.getMessage("error.message.bad.credentials", null, locale);
        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final AccountExpiredException ex,
            final Locale locale) {

        logger.debug(">>>>> AccountExpiredException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage =
                messageSource.getMessage("error.message.expired", null, locale);
        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }

    @ExceptionHandler(DisabledException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final DisabledException ex,
            final Locale locale) {

        logger.debug(">>>>> DisabledException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage =
                messageSource.getMessage("error.message.disabled", null, locale);
        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }
    
    @ExceptionHandler(CustomDisabledException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final CustomDisabledException ex,
            final Locale locale) {

        logger.debug(">>>>> CustomDisabledException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage;
        
        // Resend registration confirmation link
        if (ex.getUser() != null && !StringUtils.isEmpty(ex.getUser().getLogin())
                && !StringUtils.isEmpty(ex.getUser().getEmail())) {
            
            final String key = passwordEncoder.encode(ex.getUser().getId() + ex.getUser().getLogin()
                    + Constantes.TOKEN_SECRET + ex.getUser().getEmail());
            
            final UriComponents uri = ServletUriComponentsBuilder.fromServletMapping(request)
                    .pathSegment(RequestMappingUrl.RESEND_REGISTRATION_CONFIRMATION_LINK
                            .getAbsoluteUrl())
                    .queryParam(Constantes.KEY_PARAMETER, key)
                    .build();

            errorMessage = messageSource.getMessage("error.message.custom.disabled",
                    new Object[] {uri.toUriString()}, locale);

            request.getSession().setAttribute(Constantes.USER_LOGIN, ex.getUser().getLogin());
            
        } else {
            errorMessage = messageSource.getMessage("error.message.disabled", null, locale);
        }

        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final CredentialsExpiredException ex,
            final Locale locale) {

        logger.debug(">>>>> CredentialsExpiredException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage =
                messageSource.getMessage("error.message.credentials.expired", null, locale);
        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }

    @ExceptionHandler(LockedException.class)
    public ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final LockedException ex,
            final Locale locale) {

        logger.debug(">>>>> LockedException <<<<<");
        final List<String> errorMessages = new ArrayList<>(0);
        final String errorMessage =
                messageSource.getMessage("error.message.locked", null, locale);
        errorMessages.add(errorMessage);

        return handleAuthenticationException(request, errorMessages);
    }

    @ExceptionHandler(LoginModelValidationException.class)
    public ModelAndView handleAuthenticationException(
            final LoginModelValidationException ex) {

        logger.debug(">>>>> LoginModelValidationException <<<<<");
        final ModelAndView modelAndView = new ModelAndView(Page.LOGIN.getUrl());
        modelAndView.addObject(ModelAttributesConstantes.LOGIN_MODEL, ex.getLoginModel());
        logger.debug(">>>>> Login model : {} <<<<<", ex.getLoginModel());
        modelAndView.addObject(ModelAttributesConstantes.ERROR_MESSAGES, ex.getErrorMessages());
        return modelAndView;
    }

    private ModelAndView handleAuthenticationException(final HttpServletRequest request,
            final List<String> errorMessages) {

        final RedirectView view = new RedirectView(Page.LOGIN.getUrl(), true);
        view.setExposeModelAttributes(false);

        final ModelAndView modelAndView = new ModelAndView(view);
        final LoginModel loginModel = new LoginModel();

        loginModel
                .setLogin((String) request.getSession()
                        .getAttribute(Constantes.AUTHENTICATION_USER_LOGIN));
        request.getSession().removeAttribute(Constantes.AUTHENTICATION_USER_LOGIN);
        loginModel.setPassword(null);
        logger.debug(">>>>> Login model : {} <<<<<", loginModel);

        final FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        outputFlashMap.put(ModelAttributesConstantes.LOGIN_MODEL, loginModel);
        outputFlashMap.put(ModelAttributesConstantes.ERROR_MESSAGES, errorMessages);

        return modelAndView;
    }

    @ExceptionHandler(TokenValidationException.class)
    public ModelAndView handleTokenValidationException(final TokenValidationException ex) {

        logger.debug(">>>>> TokenValidationException <<<<<");
        final ModelAndView modelAndView = new ModelAndView(Page.ERROR.getUrl());
        modelAndView.addObject(ModelAttributesConstantes.MESSAGE, ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ApplicationBusinessException.class)
    public ModelAndView handleApplicationBusinessException(final ApplicationBusinessException ex) {

        logger.debug(">>>>> ApplicationBusinessException <<<<<");
        final ModelAndView modelAndView = new ModelAndView(Page.ERROR.getUrl());
        modelAndView.addObject(ModelAttributesConstantes.MESSAGE, ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ServiceException.class)
    public ModelAndView handleServiceException(final ServiceException ex) {

        logger.debug(">>>>> ServiceException <<<<<");
        final ModelAndView modelAndView = new ModelAndView(Page.ERROR.getUrl());
        modelAndView.addObject(ModelAttributesConstantes.MESSAGE, ex.getMessage());
        return modelAndView;
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(final UsernameNotFoundException ex) {
    	
    	logger.debug(">>>>> UsernameNotFoundException <<<<<");
    	logger.debug(ex.getCause().getMessage());
    }
    
}
