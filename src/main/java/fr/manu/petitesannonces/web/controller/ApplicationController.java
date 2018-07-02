package fr.manu.petitesannonces.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;

import fr.manu.petitesannonces.dto.PasswordResetToken;
import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.UserPrincipal;
import fr.manu.petitesannonces.dto.UserRole;
import fr.manu.petitesannonces.dto.VerificationToken;
import fr.manu.petitesannonces.dto.enums.EnumUtils;
import fr.manu.petitesannonces.dto.enums.Page;
import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.dto.enums.SocialMediaProvider;
import fr.manu.petitesannonces.dto.enums.UserRoleType;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.PasswordResetTokenService;
import fr.manu.petitesannonces.persistence.services.UserRoleService;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.persistence.services.VerificationTokenService;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.constantes.ModelAttributesConstantes;
import fr.manu.petitesannonces.web.converter.LocalDateEditor;
import fr.manu.petitesannonces.web.email.EmailHelper;
import fr.manu.petitesannonces.web.email.GMailHelper;
import fr.manu.petitesannonces.web.listener.OnRegistrationCompleteEvent;
import fr.manu.petitesannonces.web.model.LoginModel;
import fr.manu.petitesannonces.web.model.PasswordModel;
import fr.manu.petitesannonces.web.model.UserBasicInformationModel;
import fr.manu.petitesannonces.web.model.UserRecaptchaModel;
import fr.manu.petitesannonces.web.properties.RecaptchaProperties;
import fr.manu.petitesannonces.web.security.SocialUserFactory;
import fr.manu.petitesannonces.web.validator.UserBasicInformationModelValidator;
import fr.manu.petitesannonces.web.validator.UserRecaptchaModelValidator;

/**
 * @author Manu
 *
 */
@Controller
@RequestMapping("/")
// @SessionAttributes({"recaptchaSiteKey", "roles", "facebookScope"})
@SessionAttributes({"userId"})
public class ApplicationController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("userRoleService")
	private UserRoleService userRoleService;
	
	@Autowired
	@Qualifier("verificationTokenService")
	private VerificationTokenService verificationTokenService;

    @Autowired
    @Qualifier("passwordResetTokenService")
    private PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	private AuthenticationTrustResolver authenticationTrustResolver;

	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;

	@Autowired
	@Qualifier("userRecaptchaModelValidator")
	private UserRecaptchaModelValidator userRecaptchaModelValidator;

	@Autowired
	@Qualifier("userBasicInformationModelValidator")
	private UserBasicInformationModelValidator userBasicInformationModelValidator;

	@Autowired
	@Qualifier("converter")
	private Converter converter;

	@Autowired
	private RecaptchaProperties recaptchaProperties;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private SignInAdapter signInAdapter;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SocialUserFactory socialUserFactory;

    private static final String GMAIL_CALLBACK_URI = "auth/gmail/oauth2Callback";
    private static final String LOG_START = ">>>>> start <<<<<";
    private static final String LOG_START_PARAM = ">>>>> start : {} <<<<<";
    private static final String LOG_END = ">>>>> end <<<<<";
    private static final String LOG_END_PARAM = ">>>>> end : {} <<<<<";
    private static final String LOG_EXCEPTION = ">>>>> Exception : {} <<<<<";
    private static final String GMAIL_UNAVAILABLE = ">>>>> Gmail service unavailable <<<<<";
    private static final String EDIT_UNAUTHORIZED =
            ">>>>> Login unauthorized to edit user {}. Redirecting to user edit. <<<<<";
    private static final String EDIT_UNAUTHORIZED_MESSAGE = "error.message.edit.unauthorized";
    private static final String USER_NOT_FOUND =
            ">>>>> User not found. Redirecting to error page. <<<<<";
    private static final String EMPTY_TOKEN =
            ">>>>> Token is empty. Redirecting to error page. <<<<<";

	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    // @ModelAttribute("recaptchaSiteKey")
    public String getRecaptchaSiteKey() {
        logger.debug("Recaptcha site key : {}", recaptchaProperties.getRecaptchaSiteKey());
        return recaptchaProperties.getRecaptchaSiteKey();
    }

    /**
     * This method will provide UserRole list to views
     * 
     * @return list of user roles
     */
    // @ModelAttribute("roles")
    public List<UserRole> initializeRoles() {

        logger.debug(LOG_START);

        try {
            final List<UserRole> roles = userRoleService.list();
            logger.debug(LOG_END_PARAM, roles);
            return roles;
        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return new ArrayList<>(0);
        }
    }

	/*
	 * This method will list all existing users.
	 */
	@RequestMapping(value = "/admin/list", method = RequestMethod.GET)
	public String list(final Model model) {

        logger.debug(LOG_START);

		try {
			final List<User> users = userService.list();
            model.addAttribute(ModelAttributesConstantes.USERS, users);
            model.addAttribute(ModelAttributesConstantes.LOGGED_IN_USER,
                    getPrincipal().getUsername());

            logger.debug(LOG_END);

			return Page.USER_LIST.getUrl();

        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
			return Page.ERROR.getUrl();
		}
	}

	/*
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/register" }, method = RequestMethod.GET)
	public String newUser(final Model model, final NativeWebRequest webRequest) {

        logger.debug(LOG_START);

        // Redirect if user is logged in
        if (!isCurrentAuthenticationAnonymous()) {
            return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.USER_EDIT.getUrl());
        }

        UserRecaptchaModel userRecaptchaModel = new UserRecaptchaModel();
		final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);

		if (connection != null) {

            try {
                User socialUser = socialUserFactory.getUserFromSocialProviderConnection(connection);
                userRecaptchaModel = converter.convert(socialUser, UserRecaptchaModel.class);
                
            } catch (BusinessException | TechnicalException e) {
                logger.error(LOG_EXCEPTION, e.getMessage(), e);
            }
        }

        logger.debug(">>>>> UserRecaptchaModel=[{}] <<<<<", userRecaptchaModel);

        model.addAttribute(ModelAttributesConstantes.USER_MODEL, userRecaptchaModel);
        model.addAttribute(ModelAttributesConstantes.LOCALE,
                LocaleContextHolder.getLocale().getLanguage());
        // TODO : constante
        model.addAttribute("recaptchaSiteKey", getRecaptchaSiteKey());

        logger.debug(LOG_END_PARAM, model);

		return Page.REGISTER.getUrl();
	}

	/*
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public String createUser(@ModelAttribute("userModel") final UserRecaptchaModel userRecaptchaModel,
			final BindingResult result, final Model model, final NativeWebRequest webRequest, 
			final HttpServletRequest request, final Locale locale) { // @Valid

        logger.debug(LOG_START_PARAM, userRecaptchaModel);

        model.addAttribute(ModelAttributesConstantes.LOCALE,
                LocaleContextHolder.getLocale().getLanguage());
        // TODO : constante
        model.addAttribute("recaptchaSiteKey", getRecaptchaSiteKey());

        // TODO : constante
		final String recaptchaResponse = webRequest.getParameter("g-recaptcha-response");
		userRecaptchaModel.setRecaptchaResponse(recaptchaResponse);

		userRecaptchaModelValidator.validate(userRecaptchaModel, result);

		if (result.hasErrors()) {
			for (FieldError field : result.getFieldErrors()) {
                logger.debug(">>>>> field on error [{}] <<<<<", field.getField());
			}
			return Page.REGISTER.getUrl();
		}

		try {
			// Merge current user (DTO) with values to create (model)
			final User userToCreate = converter.convert(userRecaptchaModel, User.class);
			// TODO : implicit signup ?
            userToCreate.setEmailConfirmed(false);

            // Social registration
			final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
			if (connection != null) {
				final UserProfile socialMediaProfile = connection.fetchUserProfile();
                final ConnectionKey providerKey = connection.getKey();

                logger.debug(">>>>> socialMediaProfile : {} <<<<<", socialMediaProfile);

				// google, facebook, twitter
				userToCreate.setSignInProvider(SocialMediaProvider.valueOf(providerKey.getProviderId().toUpperCase()));

				// ID of User on google, facebook, twitter.
				userToCreate.setSignInProviderUserId(providerKey.getProviderUserId());

				// If the user is signing in by using a social provider, this method
				// call stores the connection to the UserConnection table.
				// Otherwise, this method does not do anything.
				providerSignInUtils.doPostSignUp(userToCreate.getLogin(), webRequest);
			}

			final User userCreated = userService.create(userToCreate);

            // Send activation email
            final UriComponents appUrl = ServletUriComponentsBuilder
	                .fromServletMapping(request)
	                .build();
            eventPublisher.publishEvent(
                    new OnRegistrationCompleteEvent(userCreated, webRequest.getLocale(), appUrl.toString()));

            // After register, logs the user in (if user is enabled).
            signInAdapter.signIn(userCreated.getId().toString(), connection, webRequest);

            model.addAttribute(ModelAttributesConstantes.SUCCESS,
            		messageSource.getMessage("success.message.user.registration", new Object[]{userCreated.getLogin(), userCreated.getEmail()}, locale));
            logger.debug(LOG_END);

			return Page.REGISTER_SUCCESS.getUrl();

        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
	}

	/*
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/user/edit" }, method = RequestMethod.GET)
    public String editUser(final HttpServletRequest request, final Model model,
            final Locale locale) {
        return this.editUser(getPrincipal().getUsername(), request, model, locale);
	}

	/*
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/user/edit" }, method = RequestMethod.POST)
	public String updateUser(@ModelAttribute("userModel") final UserBasicInformationModel userModel,
            final BindingResult result, @ModelAttribute("userId") final Long userId,
            final Model model, final HttpServletRequest request, final SessionStatus sessionStatus,
            final Locale locale) { // @Valid
        return this.updateUser(getPrincipal().getUsername(), userModel, result, userId, model,
                request, sessionStatus, locale);
	}

	/*
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/admin/edit/{login}" }, method = RequestMethod.GET)
    public String editUser(@PathVariable final String login, final HttpServletRequest request,
            final Model model, final Locale locale) {

        logger.debug(LOG_START_PARAM, login);

        model.addAttribute(ModelAttributesConstantes.LOGGED_IN_USER, getPrincipal().getUsername());

		// User must be ADMIN to edit another account
		if (StringUtils.isNotEmpty(login)
				&& !StringUtils.equalsIgnoreCase(login.trim(), getPrincipal().getUsername().trim())
				&& !request.isUserInRole(UserRoleType.ADMIN.getUserProfileType())) {
            logger.error(EDIT_UNAUTHORIZED, login);
            model.addAttribute(ModelAttributesConstantes.MESSAGE,
                    messageSource.getMessage(EDIT_UNAUTHORIZED_MESSAGE,
                            null, locale));
			return Page.ERROR.getUrl();
		}

		try {
			// Retrieve user info
			final User user = userService.getByLogin(login);
			
			if (user == null || StringUtils.isEmpty(user.getLogin())) {
                logger.error(USER_NOT_FOUND);
                return Page.ERROR.getUrl();
            }

			// Merge current user (DTO) with model
			final UserBasicInformationModel userModel = converter.convert(user, UserBasicInformationModel.class);

			// Set password to null and email confirmation to email
			userModel.setConfirmEmail(userModel.getEmail());

			// ADMIN edit ?
			boolean isAdminEdit = false;
			if (request.isUserInRole(UserRoleType.ADMIN.getUserProfileType()) && StringUtils.isNotEmpty(login)
					&& !StringUtils.equalsIgnoreCase(login.trim(), getPrincipal().getUsername().trim())) {
				isAdminEdit = true;
			}

            model.addAttribute(ModelAttributesConstantes.USER_MODEL, userModel);
            model.addAttribute(ModelAttributesConstantes.USER_ID, userModel.getId());
			if (isAdminEdit) {
                model.addAttribute(ModelAttributesConstantes.ADMIM_EDIT, true);
                model.addAttribute(ModelAttributesConstantes.LOGIN, login);
			}

            logger.debug(LOG_END_PARAM, user);
			return Page.USER_EDIT.getUrl();

        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
	}

	/*
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/admin/edit/{login}" }, method = RequestMethod.POST)
	public String updateUser(@PathVariable final String login,
            @ModelAttribute("userModel") final UserBasicInformationModel userModel,
            final BindingResult result, @ModelAttribute("userId") final Long userId,
            final Model model, final HttpServletRequest request, final SessionStatus sessionStatus,
            final Locale locale) {

        logger.debug(LOG_START_PARAM, login);

        model.addAttribute(ModelAttributesConstantes.LOGGED_IN_USER, getPrincipal().getUsername());

        // Remove from SessionAttributes (userId)
        userModel.setId(userId);
        sessionStatus.setComplete();

		// User must be ADMIN to edit another account
		if (StringUtils.isNotEmpty(login)
				&& !StringUtils.equalsIgnoreCase(login.trim(), getPrincipal().getUsername().trim())
				&& !request.isUserInRole(UserRoleType.ADMIN.getUserProfileType())) {
            logger.error(EDIT_UNAUTHORIZED, login);
            model.addAttribute(ModelAttributesConstantes.MESSAGE,
                    messageSource.getMessage(EDIT_UNAUTHORIZED_MESSAGE, null, locale));
			return Page.ERROR.getUrl();
		}

		userBasicInformationModelValidator.validate(userModel, result);

		if (result.hasErrors()) {
			for (FieldError field : result.getFieldErrors()) {
                logger.debug(">>>>> field on error [{}] <<<<<", field.getField());
			}
            model.addAttribute(ModelAttributesConstantes.USER_MODEL, userModel);
			return Page.USER_EDIT.getUrl();
		}

		try {
			// Get the current user to update
			final User userToUpdate = userService.getByLogin(login);

            if (userToUpdate == null || StringUtils.isEmpty(userToUpdate.getLogin())
                    || userId == null || !userId.equals(userToUpdate.getId())) {
                logger.error(">>>>> Invalid user. Redirecting to user edit. <<<<<");
				return Page.USER_EDIT.getUrl();
			}

			// ADMIN edit ?
			boolean isAdminEdit = false;
			if (request.isUserInRole(UserRoleType.ADMIN.getUserProfileType()) && StringUtils.isNotEmpty(login)
					&& !StringUtils.equalsIgnoreCase(login.trim(), getPrincipal().getUsername().trim())) {
				isAdminEdit = true;
			}

			if (isAdminEdit) {
                model.addAttribute(ModelAttributesConstantes.ADMIM_EDIT, true);
                model.addAttribute(ModelAttributesConstantes.LOGIN, login);
			}

			// Merge current user (DTO) with values to update (model)
			final User userMerged = converter.convert(userModel, userToUpdate);

			// Update user
			final User userUpdated = userService.update(userMerged);

            model.addAttribute(ModelAttributesConstantes.SUCCESS,
                    messageSource.getMessage("success.message.user.edit", null, locale));

            logger.debug(LOG_END_PARAM, userUpdated);
			return Page.USER_EDIT_SUCCESS.getUrl();

        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
	}

	/*
	 * This method will delete a user by it's login value.
	 */
	@RequestMapping(value = { "/admin/delete/{login}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable final String login, final HttpServletRequest request,
            final Model model, final Locale locale) {

        logger.debug(">>>>> start : " + login + " <<<<<");

		if (login == null || login.isEmpty()) {
			logger.error(">>>>> Login null or empty in deleteUser <<<<<");
			return Page.ERROR.getUrl();
		}
		
		if (!request.isUserInRole(UserRoleType.ADMIN.getUserProfileType())) {
            logger.error(EDIT_UNAUTHORIZED, login);
            model.addAttribute(ModelAttributesConstantes.MESSAGE,
                    messageSource.getMessage(EDIT_UNAUTHORIZED_MESSAGE, null, locale));
			return Page.ERROR.getUrl();
		}

		try {
            final User user = userService.getByLogin(login);
            userService.delete(user == null ? null : user.getId());
            logger.debug(LOG_END);
            return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.ADMIN_LIST.getUrl());
			
        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
	}

    /**
     * This method handles Access-Denied redirect.
     * @param model the model
     * @return url
     */
	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	public String accessDenied(final Model model) {
        logger.debug(LOG_START);
        model.addAttribute(ModelAttributesConstantes.LOGGED_IN_USER, getPrincipal().getUsername());
        logger.debug(LOG_END);
		return Page.ACCESS_DENIED.getUrl();
	}

	/**
	 * This method handles login GET requests. If users is already logged-in and
	 * tries to goto login page again, will be redirected to list page.
	 * @param loginModel the loginModel
	 * @param locale the locale
	 * @return url
	 */
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login(@ModelAttribute("loginModel") final LoginModel loginModel, final Model model, 
			final Locale locale) {

        logger.debug(">>>>> Welcome ! The client locale is : {} - Login model : {} <<<<<", locale, loginModel);

		final String targetUrl;
		if (isCurrentAuthenticationAnonymous()) {
			targetUrl = Page.LOGIN.getUrl();
		} else {
            targetUrl =
                    String.format(Constantes.REDIRECT_TO, RequestMappingUrl.ADMIN_LIST.getUrl());
		}
		
		final String successMessage = (String) model.asMap().get(ModelAttributesConstantes.SUCCESS_MESSAGE);
		if (!StringUtils.isEmpty(successMessage)) {
			model.addAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE, successMessage);
		}

        logger.debug(LOG_END_PARAM, targetUrl);
		return targetUrl;
	}
	
	@RequestMapping(value = { "/login-failure" }, method = RequestMethod.GET)
	public String loginFailure(final HttpServletRequest request) {

        logger.debug(LOG_START);

		final AuthenticationException authenticationException = (AuthenticationException) request.getSession()
                        .getAttribute(Constantes.AUTHENTICATION_LAST_EXCEPTION);
        request.getSession().removeAttribute(Constantes.AUTHENTICATION_LAST_EXCEPTION);
		
		if (authenticationException != null) {
			logger.error(">>>>> AuthenticationException : [{}] <<<<<", authenticationException);
            throw authenticationException;
		}

        return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGIN_ERROR.getUrl());
	}

	/**
	 * Redirects request forward to the registration page. This hack is required
	 * because there is no way to set the sign in url to the SocialAuthenticationFilter class.
	 * @return url
	 */
    @RequestMapping(value = {"/signin"}, method = RequestMethod.GET)
    public String socialProviderSignin() {
    	
        logger.debug(">>>>> Signin : redirecting request to edit/register user page <<<<<");
        
        // TODO test user found
        final String targetUrl;
        if (isCurrentAuthenticationAnonymous()) {
            // Social provider user connected but not found in DB
            targetUrl = String.format(Constantes.REDIRECT_TO, RequestMappingUrl.REGISTER.getUrl());
        } else {
            targetUrl = String.format(Constantes.REDIRECT_TO, RequestMappingUrl.USER_EDIT.getUrl());
        }

        return targetUrl;
	}

    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public String socialProviderSignup() {

        logger.debug(">>>>> Signup : redirecting request to edit/register user page <<<<<");

        // TODO test user found
        final String targetUrl;
        if (isCurrentAuthenticationAnonymous()) {
            // Social provider user connected but not found in DB
            targetUrl = String.format(Constantes.REDIRECT_TO, RequestMappingUrl.REGISTER.getUrl());
        } else {
            targetUrl = String.format(Constantes.REDIRECT_TO, RequestMappingUrl.USER_EDIT.getUrl());
        }

        return targetUrl;
    }
	
	/**
	 * This method handles logout requests. Toggle the handlers if you are
	 * RememberMe functionality is useless in your app.
	 * 
	 * @param request the request
	 * @param response the response
	 * @return url
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(final HttpServletRequest request, final HttpServletResponse response) {

        logger.debug(LOG_START);

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}

        logger.debug(LOG_END);
        return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGOUT_SUCESSFUL.getUrl());
	}

	@RequestMapping(value = { "/auth/gmail/oauth2Callback" }, method = RequestMethod.GET)
	public ModelAndView gmailOauth2Callback(@RequestParam(value = "code") String code, final HttpServletRequest request) {

		final UriComponents errorUri = ServletUriComponentsBuilder
                .fromServletMapping(request)
                .pathSegment(Page.ERROR.getUrl())
                .build();
		final RedirectView errorView = new RedirectView(errorUri.toUriString(), true);
		
        final UriComponents gmailOauth2CallbackUri = ServletUriComponentsBuilder
                .fromServletMapping(request).pathSegment(GMAIL_CALLBACK_URI).build();

		try {
            Gmail service = GMailHelper.getGmailService(gmailOauth2CallbackUri.toString(), code);
			if (service == null) {
                logger.error(GMAIL_UNAVAILABLE);
				return new ModelAndView(errorView);
			}
			
			RedirectView view = testGmail(request, service);
			view.setExposeModelAttributes(false);
			return new ModelAndView(view);
			
		} catch (IOException | MessagingException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
			return new ModelAndView(errorView);
		}
	}
	
	@RequestMapping(value = "/registrationConfirmation", method = RequestMethod.GET)
    public String registrationConfirmation(final Model model, @RequestParam(Constantes.TOKEN_PARAMETER) final String token,
            final RedirectAttributes redirectAttributes) {
		
	    try {
            if (StringUtils.isEmpty(token)) {
                logger.error(EMPTY_TOKEN);
                return Page.ERROR.getUrl();
            }

	    	final VerificationToken verificationToken = verificationTokenService.getByToken(token);
		    
		    if (verificationToken == null) {
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.INVALID, true);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.EXPIRED, false);
		    	logger.debug(">>>>> Token invalid : redirecting to /registrationConfirmationError <<<<<");
                return String.format(Constantes.REDIRECT_TO,
                        RequestMappingUrl.REGISTRATION_CONFIRMATION_ERROR.getUrl());
		    }
		     
		    // Expiry date must be > now
		    if (LocalDateTime.now().compareTo(verificationToken.getExpiryDate()) > 0) {
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.INVALID, false);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.EXPIRED, true);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.TOKEN, token);
		    	logger.debug(">>>>> Token expired : redirecting to /registrationConfirmationError <<<<<");
                return String.format(Constantes.REDIRECT_TO,
                        RequestMappingUrl.REGISTRATION_CONFIRMATION_ERROR.getUrl());
		    }
		    
		    final User user = verificationToken.getUser();
		    user.setEmailConfirmed(true); 
			userService.update(user);
			
			redirectAttributes.addFlashAttribute(ModelAttributesConstantes.USER, user);
            return String.format(Constantes.REDIRECT_TO,
                    RequestMappingUrl.REGISTRATION_CONFIRMATION_SUCCESS.getUrl());
			
        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
	}
	
	@RequestMapping(value = "/registrationConfirmationError", method = RequestMethod.GET)
    public String registrationConfirmationError(final Model model, final HttpServletRequest request,
            final Locale locale) {
		
		logger.debug(">>>>> registrationConfirmationError <<<<<");
        final Boolean expired = (Boolean) model.asMap().get(ModelAttributesConstantes.EXPIRED);
        final String message = expired
                ? messageSource.getMessage("message.registration.confirmation.token.expired", null,
                        locale)
                : messageSource.getMessage("message.registration.confirmation.invalid.token", null,
                        locale);
        model.addAttribute(ModelAttributesConstantes.MESSAGE, message);
        return Page.REGISTRATION_CONFIRMATION_ERROR.getUrl();
	}
	
	@RequestMapping(value = "/registrationConfirmationSuccess", method = RequestMethod.GET)
    public String registrationConfirmationSuccess(final Model model,
            final HttpServletRequest request, final Locale locale) {

        logger.debug(LOG_START);
		final User user = (User) model.asMap().get(ModelAttributesConstantes.USER);
		final String message = messageSource.getMessage("success.message.user.activation", new Object[]{user.getLogin()}, locale);
        model.addAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE, message);
        return Page.REGISTRATION_CONFIRMATION_SUCCESS.getUrl();
	}
	
    @RequestMapping(value = "/resendRegistrationConfirmationLink", method = RequestMethod.GET)
    public String resendRegistrationToken(
            @RequestParam(value = Constantes.TOKEN_PARAMETER, required = false) final String token,
            @RequestParam(value = Constantes.KEY_PARAMETER, required = false) final String key,
            final HttpServletRequest request, final RedirectAttributes redirectAttributes, final Locale locale) {

        logger.debug(LOG_START);

        try {
            final User user;
            
            if (!StringUtils.isEmpty(token)) {
            	// Retrieve user info
            	final VerificationToken verificationToken = verificationTokenService.getByToken(token);

                if (verificationToken == null || verificationToken.getUser() == null) {
                    logger.error(">>>>> Verification token not found <<<<<");
                    return Page.ERROR.getUrl();
                }

                user = verificationToken.getUser();
            	
            } else if (!StringUtils.isEmpty(key)) {
                String login = (String) request.getSession().getAttribute(Constantes.USER_LOGIN);
                request.getSession().removeAttribute(Constantes.USER_LOGIN);

                if (StringUtils.isEmpty(login)) {
                    logger.error(">>>>> Login not found. Redirecting to error page. <<<<<");
                    return Page.ERROR.getUrl();
                }

                // Get the current user
                user = userService.getByLogin(login);
                
            } else {
                logger.error(
                        ">>>>> Token or key not found. Redirecting to error page. <<<<<");
                return Page.ERROR.getUrl();
            }
            
            if (user == null || user.getId() == null || StringUtils.isEmpty(user.getLogin())
                    || StringUtils.isEmpty(user.getEmail())) {
                logger.error(USER_NOT_FOUND);
                return Page.ERROR.getUrl();
            }
            
            logger.debug(">>>>> user : [{}] <<<<<", user);

            if (!StringUtils.isEmpty(key)) {
                final String rawKey =
                        user.getId() + user.getLogin() + Constantes.TOKEN_SECRET + user.getEmail();

                if (!passwordEncoder.matches(rawKey, key)) {
                    logger.error(">>>>> Keys don't match. Redirecting to error page. <<<<<");
                    return Page.ERROR.getUrl();
                }
            }

            // Create new token
            final String newToken = UUID.randomUUID().toString();
            verificationTokenService.create(user, newToken);

            final Gmail service = GMailHelper.getGmailService();
            if (service == null) {
                logger.error(GMAIL_UNAVAILABLE);
            	return Page.ERROR.getUrl();
            }

            final UriComponents uri = ServletUriComponentsBuilder.fromServletMapping(request)
                    .pathSegment(RequestMappingUrl.REGISTRATION_CONFIRMATION.getAbsoluteUrl())
                    .queryParam(Constantes.TOKEN_PARAMETER, newToken).build();
            final String confirmationUrl = uri.toUriString();

            // TODO Mock : EmailHelper.send(service, user.getEmail(), null,
            // "emmanuel.mura@gmail.com", "Registration Confirmation", confirmationUrl);
            EmailHelper.send(service, "emmanuel.mura@gmail.com", null, "emmanuel.mura@gmail.com",
                    "Registration Confirmation", confirmationUrl);

            redirectAttributes.addFlashAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE,
                    messageSource.getMessage("success.message.user.email.sent",
                            new Object[] {user.getEmail()}, locale));
            return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGIN.getUrl());

        } catch (BusinessException | TechnicalException | IOException | MessagingException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(),
                    e);
            return Page.ERROR.getUrl();
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String resetPassword() {
        logger.debug(">>>>> resetPassword <<<<<");
        return Page.RESET_PASSWORD.getUrl();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String sendResetPasswordLink(@RequestParam("email") final String email,
            final Model model, final HttpServletRequest request,
            final RedirectAttributes redirectAttributes, final Locale locale) {

        logger.debug(LOG_START);

        try {
            if (StringUtils.isEmpty(email)) {
                logger.error(">>>>> Email is empty. Redirecting to error page. <<<<<");
                return Page.ERROR.getUrl();
            }

            final User user = userService.getByEmail(email);

            if (user == null) {
                logger.debug(">>>>> Email invalid <<<<<");
                model.addAttribute(ModelAttributesConstantes.MESSAGE,
                        messageSource.getMessage("error.message.email.not.found", null, locale));
                return Page.RESET_PASSWORD.getUrl();

            } else if (!user.getEmailConfirmed()) {
                logger.debug(">>>>> Email not confirmed <<<<<");
                model.addAttribute(ModelAttributesConstantes.MESSAGE, messageSource
                        .getMessage("error.message.email.not.confirmed", null, locale));
                return Page.RESET_PASSWORD.getUrl();
            }

            final String token = UUID.randomUUID().toString();
            passwordResetTokenService.create(user, token);

            final Gmail service = GMailHelper.getGmailService();
            if (service == null) {
                logger.error(GMAIL_UNAVAILABLE);
                return Page.RESET_PASSWORD.getUrl();
            }

            final UriComponents uri = ServletUriComponentsBuilder.fromServletMapping(request)
                    .pathSegment("changePassword").queryParam(Constantes.TOKEN_PARAMETER, token)
                    .queryParam("key", user.getId()).build();
            final String confirmationUrl = uri.toUriString();

            // TODO Mock : EmailHelper.send(service, user.getEmail(), null,
            // "emmanuel.mura@gmail.com",
            // "Reset Password", confirmationUrl);
            EmailHelper.send(service, "emmanuel.mura@gmail.com", null, "emmanuel.mura@gmail.com",
                    "Reset Password", confirmationUrl);

            redirectAttributes.addFlashAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE,
                    messageSource.getMessage("success.message.user.email.sent",
                            new Object[] {user.getEmail()}, locale));
            return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGIN.getUrl());

        } catch (BusinessException | TechnicalException | IOException | MessagingException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String resetPasswordValidation(@RequestParam(Constantes.KEY_PARAMETER) final Long id,
            @RequestParam(Constantes.TOKEN_PARAMETER) final String token, final Model model,
            final RedirectAttributes redirectAttributes, final Locale locale) {

        logger.debug(LOG_START);

        try {
            if (StringUtils.isEmpty(token)) {
                logger.error(EMPTY_TOKEN);
                return Page.ERROR.getUrl();
            }

            final PasswordResetToken passwordResetToken =
                    passwordResetTokenService.getByToken(token);

            if (passwordResetToken == null || passwordResetToken.getUser() == null
                    || passwordResetToken.getUser().getId() == null
                    || !passwordResetToken.getUser().getId().equals(id)) {
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.INVALID, true);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.EXPIRED, false);
                logger.debug(">>>>> Token invalid : redirecting to /changePasswordError <<<<<");
                return String.format(Constantes.REDIRECT_TO,
                        RequestMappingUrl.CHANGE_PASSWORD_ERROR.getUrl());
            }

            // Expiry date must be > now
            if (LocalDateTime.now().compareTo(passwordResetToken.getExpiryDate()) > 0) {
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.INVALID, false);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.EXPIRED, true);
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.TOKEN, token);
                logger.debug(">>>>> Token expired : redirecting to /changePasswordError <<<<<");
                return String.format(Constantes.REDIRECT_TO,
                        RequestMappingUrl.CHANGE_PASSWORD_ERROR.getUrl());
            }

            final User user = passwordResetToken.getUser();

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                    Arrays.asList(
                            new SimpleGrantedAuthority(Constantes.CHANGE_PASSWORD_PRIVILEGE)));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication.getPrincipal() == null) {
                List<String> errorMessages = Arrays.asList(
                        messageSource.getMessage("error.message.user.not.found", null, locale));
                redirectAttributes.addFlashAttribute(ModelAttributesConstantes.ERROR_MESSAGES,
                        errorMessages);
                return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGIN.getUrl());
            }

            return String.format(Constantes.REDIRECT_TO,
                    RequestMappingUrl.UPDATE_PASSWORD.getUrl());

        } catch (BusinessException | TechnicalException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(),
                    e);
            return Page.ERROR.getUrl();
        }
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public String changePassword(final Model model, final Locale locale) {
        
        logger.debug(LOG_START);
        
        UserPrincipal principal = getPrincipal();

        if (principal.getUsername().isEmpty()) {
            return Page.ERROR.getUrl();
        }

        model.addAttribute(ModelAttributesConstantes.PASSWORD_MODEL, new PasswordModel());
        return Page.CHANGE_PASSWORD.getUrl();
    }
    
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(
            @Valid @ModelAttribute("passwordModel") final PasswordModel passwordModel,
            final BindingResult result, final Model model, final Locale locale) {

        try {
            logger.debug(LOG_START);

            if (result.hasErrors()) {
                final List<String> errorMessages = new ArrayList<>(0);

                for (ObjectError error : result.getAllErrors()) {
                    final String errorMessage =
                            messageSource.getMessage(error, LocaleContextHolder.getLocale());
                    logger.debug(">>>>> Error message : {} <<<<<", errorMessage);

                    if (errorMessage != null && !errorMessage.isEmpty()) {
                        errorMessages.add(errorMessage);
                    }
                }
                
                model.addAttribute(ModelAttributesConstantes.ERROR_MESSAGES, errorMessages);
                model.addAttribute(ModelAttributesConstantes.PASSWORD_MODEL, passwordModel);
                return Page.CHANGE_PASSWORD.getUrl();
            }

            UserPrincipal principal = getPrincipal();

            if (principal.getUsername().isEmpty()) {
                logger.error(">>>>> Principal not found. Redirecting to error page. <<<<<");
                return Page.ERROR.getUrl();
            }

            // Update password
            // Get the current user to update
            final User userToUpdate = userService.getByLogin(principal.getUsername());

            if (userToUpdate == null || StringUtils.isEmpty(userToUpdate.getLogin())) {
                logger.error(USER_NOT_FOUND);
                return Page.ERROR.getUrl();
            }

            // Encode password
            userToUpdate.setPassword(passwordEncoder.encode(passwordModel.getPassword()));

            // Update user
            final User userUpdated = userService.update(userToUpdate);

            logger.debug(">>>>> User updated : {} <<<<<", userUpdated);

            // Remove authorities
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            model.addAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE,
                    messageSource.getMessage("message.reset.password.successful", null, locale));

            return Page.CHANGE_PASSWORD_SUCCESS.getUrl();

        } catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> Exception in updatePassword : {} <<<<<", e.getMessage(), e);
            return Page.ERROR.getUrl();
        }
    }

    @RequestMapping(value = "/changePasswordError", method = RequestMethod.GET)
    public String changePasswordError(final Model model, final HttpServletRequest request,
            final Locale locale) {

        logger.debug(LOG_START);

        Boolean expired = (Boolean) model.asMap().get(ModelAttributesConstantes.EXPIRED);
        String message = expired
                ? messageSource.getMessage("message.reset.password.token.expired", null,
                        locale)
                : messageSource.getMessage("message.reset.password.invalid.token", null,
                        locale);
        model.addAttribute(ModelAttributesConstantes.MESSAGE, message);

        return Page.CHANGE_PASSWORD_ERROR.getUrl();
    }
    
    @RequestMapping(value = "/resendPasswordResetLink", method = RequestMethod.GET)
    public String resendPasswordResetToken(@RequestParam(Constantes.TOKEN_PARAMETER) final String token,
            final HttpServletRequest request, final RedirectAttributes redirectAttributes,
            final Locale locale) {

        logger.debug(LOG_START);

        try {
            if (StringUtils.isEmpty(token)) {
                logger.error(EMPTY_TOKEN);
                return Page.ERROR.getUrl();
            }

            // Retrieve user info
            final PasswordResetToken passwordResetToken =
                    passwordResetTokenService.getByToken(token);

            if (passwordResetToken == null || passwordResetToken.getUser() == null) {
                logger.error(">>>>> Password reset token not found <<<<<");
                return Page.ERROR.getUrl();
            }

            logger.debug(">>>>> user {} - token {} <<<<<", passwordResetToken.getUser(), token);

            final String newToken = UUID.randomUUID().toString();
            passwordResetTokenService.create(passwordResetToken.getUser(), newToken);

            final Gmail service = GMailHelper.getGmailService();
            if (service == null) {
                logger.error(GMAIL_UNAVAILABLE);
                return Page.ERROR.getUrl();
            }

            final UriComponents uri = ServletUriComponentsBuilder.fromServletMapping(request)
                    .pathSegment("changePassword").queryParam(Constantes.TOKEN_PARAMETER, newToken)
                    .queryParam(Constantes.KEY_PARAMETER, passwordResetToken.getUser().getId())
                    .build();
            final String confirmationUrl = uri.toUriString();

            // TODO Mock : EmailHelper.send(service, user.getEmail(), null,
            // "emmanuel.mura@gmail.com",
            // "Reset Password", confirmationUrl);
            EmailHelper.send(service, "emmanuel.mura@gmail.com", null, "emmanuel.mura@gmail.com",
                    "Reset Password", confirmationUrl);

            redirectAttributes.addFlashAttribute(ModelAttributesConstantes.SUCCESS_MESSAGE,
                    messageSource.getMessage("success.message.user.email.sent",
                            new Object[] {passwordResetToken.getUser().getEmail()}, locale));
            return String.format(Constantes.REDIRECT_TO, RequestMappingUrl.LOGIN.getUrl());
            
        } catch (BusinessException | TechnicalException | IOException | MessagingException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(),
                    e);
            return Page.ERROR.getUrl();
        }
    }

    /**
     * Binding for joda time input fields
     * 
     * @param binder the WebDataBinder
     */
    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
        // binder.setValidator(new UserModelValidator());
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     * 
     * @return principal
     */
    private UserPrincipal getPrincipal() {

        logger.debug(LOG_START);

        final UserPrincipal userPrincipal = new UserPrincipal();
        String userName = null;
        final List<UserRoleType> roles = new ArrayList<>();

        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null) {

            final Object principal =
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof User) {
                userName = ((User) principal).getLogin();
            } else {
                userName = principal.toString();
            }

            final Collection<? extends GrantedAuthority> authorities =
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            for (final GrantedAuthority authority : authorities) {
                if (authority != null) {
                    final String role =
                            StringUtils.remove(authority.getAuthority(), Constantes.ROLE_PREFIX);
                    final UserRoleType userRoleType = EnumUtils.byLabel(UserRoleType.class, role);
                    roles.add(userRoleType);
                }
            }
        }

        userPrincipal.setUsername(userName);
        userPrincipal.setRoles(roles);

        logger.debug(LOG_END_PARAM, userPrincipal);
        return userPrincipal;
    }

    /**
     * This method returns true if users is already authenticated [logged-in],
     * else false.
     * 
     * @return true if users is already authenticated, else false
     */
    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    /**
     * Test method to send email via gmail
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = {"/auth/gmail"}, method = RequestMethod.GET)
    public RedirectView sendTestEmail(HttpServletRequest request) {

        final UriComponents errorUri = ServletUriComponentsBuilder.fromServletMapping(request)
                .pathSegment(Page.ERROR.getUrl()).build();
        final RedirectView errorView = new RedirectView(errorUri.toUriString(), true);

        final UriComponents gmailOauth2CallbackUri = ServletUriComponentsBuilder
                .fromServletMapping(request).pathSegment(GMAIL_CALLBACK_URI).build();

        try {
            Gmail service = GMailHelper.getGmailService();
            if (service == null) {
                AuthorizationRequestUrl url =
                        GMailHelper.getAuthorizationRequestUrl(gmailOauth2CallbackUri.toString());
                logger.debug(">>>>> Gmail service unavailable, redirecting to {} <<<<<", url);
                return new RedirectView(url.toString());
            }

            return testGmail(request, service);

        } catch (GoogleJsonResponseException e) {
            try {
                AuthorizationRequestUrl url =
                        GMailHelper.getAuthorizationRequestUrl(gmailOauth2CallbackUri.toString());
                logger.debug(">>>>> Gmail service unavailable, redirecting to {} <<<<<", url, e);
                return new RedirectView(url.toString());

            } catch (IOException ioe) {
                logger.error(">>>>> IOException : {} <<<<<", ioe.getMessage(), ioe);
                return errorView;
            }

        } catch (IOException | MessagingException e) {
            logger.error(LOG_EXCEPTION, e.getMessage(), e);
            return errorView;
        }
    }

    /**
     * Test Gmail service to send email
     * 
     * @param request the request
     * @param service the service
     * @return RedirectView
     * @throws IOException the IOException
     * @throws MessagingException the MessagingException
     */
    private RedirectView testGmail(final HttpServletRequest request, final Gmail service)
            throws IOException, MessagingException {

        final String recipientEmail = "emmanuel.mura@gmail.com";
        final String fromEmail = "emmanuel.mura@gmail.com";

        // Test send email
        EmailHelper.send(service, recipientEmail, null, fromEmail, "test", "message");
        logger.debug(">>>>> Gmail service OK, email sent to {} from {} <<<<<", recipientEmail,
                fromEmail);

        final UriComponents uri = ServletUriComponentsBuilder.fromServletMapping(request)
                .pathSegment("login").build();
        final RedirectView view = new RedirectView(uri.toUriString(), true);
        view.setExposeModelAttributes(false);

        return view;
    }
}