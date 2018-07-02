/**
 * 
 */
package fr.manu.petitesannonces.web.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.web.model.UserModel;


/**
 * @author Manu
 *
 */
@Component("userModelValidator")
public class UserModelValidator extends UserBasicInformationModelValidator implements Validator { // extends RecaptchaModelValidator
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
    @Qualifier("validator")
    private Validator validator;
	
	private static final Logger logger = LoggerFactory.getLogger(UserModelValidator.class);
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return UserModel.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		
        logger.debug(">>>>> start <<<<<");
		
		super.validate(target, errors);
		
		final UserModel userModel = (UserModel) target;
		
		validator.validate(userModel, errors);
		
		if (userModel == null) {
			errors.rejectValue("login", "error.message.user.not.empty");
		}
		
		// Password confirmation
        if (userModel != null && userModel.getPassword() != null
                && userModel.getConfirmPassword() != null
                && !userModel.getPassword().equals(userModel.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "error.message.user.password.confirmation");
		}
		
		try {
			// Check login doesn't exist
            if (userModel != null
                    && !userService.isUserLoginUnique(userModel.getId(), userModel.getLogin())) {
                errors.rejectValue("login", "error.message.user.login.non.unique",
                        new Object[] {userModel.getLogin()}, "Login already exists");
			}
			
            logger.debug(">>>>> end <<<<<");
			
        } catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> Exception <<<<<", e);
        }
	}

}
