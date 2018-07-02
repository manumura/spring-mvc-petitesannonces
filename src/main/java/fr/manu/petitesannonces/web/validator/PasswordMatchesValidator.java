package fr.manu.petitesannonces.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import fr.manu.petitesannonces.web.model.PasswordModel;
import fr.manu.petitesannonces.web.model.UserModel;

/**
 * @author emmanuel.mura
 *
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
	
	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordMatchesValidator.class);

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Nothing to do here
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
    	
    	boolean valid = false;

        logger.debug(">>>>> start : {} <<<<<", value);

        if (value instanceof UserModel) {
            final UserModel userModel = (UserModel) value;
            valid = userModel.getPassword().equals(userModel.getConfirmPassword());
        } else if (value instanceof PasswordModel) {
            final PasswordModel passwordModel = (PasswordModel) value;
            valid = passwordModel.getPassword().equals(passwordModel.getConfirmPassword());
        }
        
        logger.debug(">>>>> Password valid : {} <<<<<", valid);

        if (!valid) {
        	context.disableDefaultConstraintViolation();
        	
        	String errorMessage = messageSource.getMessage("error.message.user.confirm.password.invalid", null, LocaleContextHolder.getLocale());
        	context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }
        
        return valid;
    }

}
