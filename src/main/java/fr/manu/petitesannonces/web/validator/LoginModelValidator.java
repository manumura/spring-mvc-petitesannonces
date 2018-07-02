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

import fr.manu.petitesannonces.web.model.LoginModel;


/**
 * @author Manu
 *
 */
@Component("loginModelValidator")
public class LoginModelValidator implements Validator {
	
	@Autowired
    @Qualifier("validator")
    private Validator validator;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginModelValidator.class);
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return LoginModel.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
        logger.debug(">>>>> start <<<<<");
		final LoginModel loginModel = (LoginModel) target;
		validator.validate(loginModel, errors);
        logger.debug(">>>>> end <<<<<");
	}

}
