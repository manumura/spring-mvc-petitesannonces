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
import fr.manu.petitesannonces.web.model.UserBasicInformationModel;


/**
 * @author Manu
 *
 */
@Component("userBasicInformationModelValidator")
public class UserBasicInformationModelValidator implements Validator {
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
    @Qualifier("validator")
    private Validator validator;
	
	private static final Logger logger = LoggerFactory.getLogger(UserBasicInformationModelValidator.class);
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return UserBasicInformationModel.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		
        logger.debug(">>>>> start <<<<<");
		
		final UserBasicInformationModel userBasicInformationModel = (UserBasicInformationModel) target;
		
		validator.validate(userBasicInformationModel, errors);
		
		if (userBasicInformationModel == null) {
			errors.rejectValue("login", "error.message.user.not.empty");
		}
		
		// Email confirmation
        if (userBasicInformationModel != null && userBasicInformationModel.getEmail() != null
                && userBasicInformationModel.getConfirmEmail() != null 
				&& !userBasicInformationModel.getEmail().equals(userBasicInformationModel.getConfirmEmail())) {
			errors.rejectValue("confirmEmail", "error.message.user.email.confirmation");
		}

        try {
            // Check email doesn't exist
            if (userBasicInformationModel != null
                    && !userService.isUserEmailUnique(userBasicInformationModel.getId(),
                            userBasicInformationModel.getEmail())) {
                errors.rejectValue("email", "error.message.user.email.non.unique",
                        new Object[] {userBasicInformationModel.getEmail()},
                        "Email already exists");
            }

            logger.debug(">>>>> end <<<<<");

        } catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> Exception <<<<<", e);
        }
	}

}
