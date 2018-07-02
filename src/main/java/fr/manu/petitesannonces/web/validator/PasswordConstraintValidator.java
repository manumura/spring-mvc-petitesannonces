package fr.manu.petitesannonces.web.validator;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author emmanuel.mura
 *
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordConstraintValidator.class);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Nothing to do here
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        logger.debug(">>>>> start : {} <<<<<", password);
    	
        ResourceBundle messages =
                PropertyResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        Properties properties = convertResourceBundleToProperties(messages);
        MessageResolver resolver = new PropertiesMessageResolver(properties);
			
    	List<Rule> rules = Arrays.asList(new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule());

        PasswordValidator validator = new PasswordValidator(resolver, rules);

        RuleResult result = validator.validate(new PasswordData(password));
        logger.debug(">>>>> Result : {} <<<<<", result);

        if (result.isValid()) {
            return true;
        }
        
        context.disableDefaultConstraintViolation();
        for (String errorMessage : validator.getMessages(result)) {
        	context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }
        
        return false;
    }
    
	private Properties convertResourceBundleToProperties(ResourceBundle resource) {
		Properties properties = new Properties();

		Enumeration<String> keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			properties.put(key, resource.getString(key));
		}

		return properties;
	}

}
