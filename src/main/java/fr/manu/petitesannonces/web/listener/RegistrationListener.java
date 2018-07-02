package fr.manu.petitesannonces.web.listener;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.google.api.services.gmail.Gmail;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.VerificationTokenService;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.email.EmailHelper;
import fr.manu.petitesannonces.web.email.GMailHelper;
import fr.manu.petitesannonces.web.exceptions.RegistrationConfirmationException;

/**
 * @author emmanuel.mura
 *
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (RegistrationConfirmationException e) {
            logger.error(
                    ">>>>> RegistrationListener.onApplicationEvent : RegistrationConfirmationException - {} <<<<<",
                    e.getMessage(), e);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
            throws RegistrationConfirmationException {

        try {
            User user = event.getUser();
            String token = UUID.randomUUID().toString();
            logger.debug(">>>>> user : {} - token : {} <<<<<", user, token);

            verificationTokenService.create(user, token);
            
            Gmail service = GMailHelper.getGmailService();
            if (service == null) {
                String message = messageSource.getMessage("error.message.gmail.unavailable", null, event.getLocale());
                throw new RegistrationConfirmationException(message);
			}

            String confirmationUrl = event.getAppUrl() + RequestMappingUrl.REGISTRATION_CONFIRMATION.getUrl() + "?" + Constantes.TOKEN_PARAMETER + "=" + token;
            logger.debug(">>>>> Confirmation URL : {} <<<<<", confirmationUrl);
            
            // TODO Mock email
            // EmailHelper.send(service, user.getEmail(), null, "emmanuel.mura@gmail.com",
            // "Registration Confirmation", confirmationUrl);
            EmailHelper.send(service, "emmanuel.mura@gmail.com", null, "emmanuel.mura@gmail.com", "Registration Confirmation", confirmationUrl);

        } catch (BusinessException e) {
            String message = messageSource.getMessage("error.message.business.exception", null, event.getLocale());
            throw new RegistrationConfirmationException(message, e);
        } catch (TechnicalException e) {
            String message = messageSource.getMessage("error.message.technical.exception", null, event.getLocale());
            throw new RegistrationConfirmationException(message, e);
        } catch (IOException e) {
            String message = messageSource.getMessage("error.message.ioexception.exception", null, event.getLocale());
            throw new RegistrationConfirmationException(message, e);
		} catch (MessagingException e) {
            String message = messageSource.getMessage("error.message.messaging.exception", null, event.getLocale());
            throw new RegistrationConfirmationException(message, e);
		}
    }
}
