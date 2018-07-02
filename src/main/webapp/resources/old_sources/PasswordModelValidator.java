package fr.manu.petitesannonces.web.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.manu.petitesannonces.web.model.PasswordModel;


/**
 * @author Manu
 * @deprecated Use PasswordConstraintValidator and PasswordMatchesValidator
 *
 */
@Deprecated
@Component("passwordModelValidator")
public class PasswordModelValidator implements Validator {
	
	@Autowired
    @Qualifier("validator")
    private Validator validator;
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordModelValidator.class);
	
	@Override
	public boolean supports(final Class<?> clazz) {
        return PasswordModel.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		
        logger.debug(">>>>> start <<<<<");
		
        final PasswordModel passwordModel = (PasswordModel) target;
		
        validator.validate(passwordModel, errors);
		
        if (passwordModel == null) {
            errors.rejectValue("password", "error.message.user.password.not.empty");
		}
		
		// Password confirmation
        if (passwordModel != null && passwordModel.getPassword() != null
                && passwordModel.getConfirmPassword() != null
                && !passwordModel.getPassword().equals(passwordModel.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "error.message.user.password.confirmation");
		}
		
        logger.debug(">>>>> end <<<<<");
	}

}
