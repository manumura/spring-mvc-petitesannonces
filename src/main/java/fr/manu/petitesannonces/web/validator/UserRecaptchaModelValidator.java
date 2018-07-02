/**
 * 
 */
package fr.manu.petitesannonces.web.validator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import fr.manu.petitesannonces.persistence.exceptions.impl.RecaptchaServiceException;
import fr.manu.petitesannonces.persistence.services.RecaptchaService;
import fr.manu.petitesannonces.web.model.UserRecaptchaModel;

/**
 * @author Manu
 *
 */
@Component("userRecaptchaModelValidator")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRecaptchaModelValidator extends UserModelValidator {

	@Autowired
	@Qualifier("validator")
	private Validator validator;

	private static final String ERROR_RECAPTCHA_INVALID = "error.message.recaptcha.invalid";

	private static final String ERROR_RECAPTCHA_UNAVAILABLE = "error.message.recaptcha.unavailable";

	private static final String ERROR_RECAPTCHA_EMPTY = "error.message.recaptcha.not.empty";

	private final HttpServletRequest httpServletRequest;

	private final RecaptchaService recaptchaService;

	private static final Logger logger = LoggerFactory.getLogger(UserRecaptchaModelValidator.class);

	@Autowired
	public UserRecaptchaModelValidator(final HttpServletRequest httpServletRequest,
			final RecaptchaService recaptchaService) {
		this.httpServletRequest = httpServletRequest;
		this.recaptchaService = recaptchaService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserRecaptchaModel.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {

        logger.debug(">>>>> start <<<<<");

		super.validate(target, errors);

		try {
			final UserRecaptchaModel userRecaptchaModel = (UserRecaptchaModel) target;

			validator.validate(userRecaptchaModel, errors);

			if (userRecaptchaModel.getRecaptchaResponse() == null
					|| userRecaptchaModel.getRecaptchaResponse().isEmpty()) {
                logger.error(">>>>> recaptcha response empty <<<<<");
				// errors.reject(ERROR_RECAPTCHA_EMPTY);
				errors.rejectValue("recaptchaResponse", ERROR_RECAPTCHA_EMPTY);
			} else if (!recaptchaService.isResponseValid(this.getRemoteIp(httpServletRequest),
					userRecaptchaModel.getRecaptchaResponse())) {
                logger.error(">>>>> recaptcha invalid <<<<<");
				// errors.reject(ERROR_RECAPTCHA_INVALID);
				errors.rejectValue("recaptchaResponse", ERROR_RECAPTCHA_INVALID);
			}

            logger.debug(">>>>> end <<<<<");

		} catch (RecaptchaServiceException e) {
            logger.error(">>>>> RecaptchaServiceException <<<<<", e);
			errors.rejectValue("recaptchaResponse", ERROR_RECAPTCHA_UNAVAILABLE);
		}
	}

	/**
	 * Get real IP
	 * 
	 * @param request the request
	 * @return the remote IP
	 */
	private String getRemoteIp(final HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
