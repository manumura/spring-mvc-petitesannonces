package fr.manu.petitesannonces.web.security;

import java.util.UUID;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.enums.SocialMediaProvider;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.properties.SocialProperties;

/**
 * @author emmanuel.mura
 *
 */
@Component
public class SocialUserFactory {

    private static final Logger logger =
            LoggerFactory.getLogger(SocialUserFactory.class);

    @Autowired
    private UserService userService;
    
    @Autowired
	private SocialProperties socialProperties;

    public SocialUserFactory() {
    	super();
    }

    /**
     * Get User from social provider connection
     * 
     * @param connection
     * @return
     * @throws TechnicalException
     * @throws BusinessException
     */
    public User getUserFromSocialProviderConnection(Connection<?> connection)
            throws BusinessException, TechnicalException {

        final Boolean isImplicitSignUp = socialProperties.getIsImplicitSignp();

        final ConnectionKey providerKey = connection.getKey();
        final SocialMediaProvider socialMediaProvider = EnumUtils.getEnum(SocialMediaProvider.class,
                providerKey.getProviderId().toUpperCase());
        final UserProfile socialMediaProfile = connection.fetchUserProfile();
        
        final User user = new User();
        user.setEmailConfirmed(false);
        user.setPrenom(socialMediaProfile.getFirstName());
        user.setNom(socialMediaProfile.getLastName());
        user.setSignInProviderUserId(providerKey.getProviderUserId());
        user.setSignInProvider(
                SocialMediaProvider.valueOf(providerKey.getProviderId().toUpperCase()));
        
        final String username = StringUtils.isEmpty(socialMediaProfile.getUsername())
                ? socialMediaProfile.getFirstName() + socialMediaProfile.getLastName()
                : socialMediaProfile.getUsername();

        String login = null;
        String email = null;

        if (SocialMediaProvider.FACEBOOK.equals(socialMediaProvider)) {

            final Facebook facebook = (Facebook) connection.getApi();
            String facebookBirthdayAsString =
                    facebook.userOperations().getUserProfile().getBirthday();
            LocalDate facebookBirthday = null;

            try {
                facebookBirthday = LocalDate.parse(facebookBirthdayAsString,
                        DateTimeFormat.forPattern(Constantes.FACEBOOK_DATE_FORMAT));
            } catch (IllegalFieldValueException e1) {

                logger.error(">>>>> Error parsing facebook birthday {} as [{}] <<<<<",
                        facebookBirthdayAsString, Constantes.FACEBOOK_DATE_FORMAT, e1);

                try {
                    facebookBirthday = LocalDate.parse(facebookBirthdayAsString,
                            DateTimeFormat.forPattern(Constantes.FACEBOOK_YEAR_FORMAT));
                } catch (IllegalFieldValueException e2) {

                    logger.error(">>>>> Error parsing facebook birthday {} as [{}] <<<<<",
                            facebookBirthdayAsString, Constantes.FACEBOOK_YEAR_FORMAT, e2);

                    try {
                        facebookBirthday = LocalDate.parse(facebookBirthdayAsString, DateTimeFormat
                                .forPattern(Constantes.FACEBOOK_MONTH_AND_DAY_FORMAT));
                    } catch (IllegalFieldValueException e3) {
                        logger.error(">>>>> Error parsing facebook birthday {} as [{}] <<<<<",
                                facebookBirthdayAsString, Constantes.FACEBOOK_MONTH_AND_DAY_FORMAT,
                                e3);
                    }
                }
            }

            if (facebookBirthday != null) {
                user.setDateNaissance(facebookBirthday);
            }

            // Email
            email = socialMediaProfile.getEmail();
            // Login
            login = StringUtils.isEmpty(email) ? username.toLowerCase()
                    : email.substring(0, email.indexOf('@'));

        } else if (SocialMediaProvider.TWITTER.equals(socialMediaProvider)) {
            final Twitter twitter = (Twitter) connection.getApi();

            twitter.userOperations().getUserProfile().getProfileUrl();

            // Login
            login = username.toLowerCase();
            // Email placeholder
            if (isImplicitSignUp) {
            	email = username.toLowerCase() + "@twitter.com";
            }
            
            user.setPrenom(socialMediaProfile.getName());
            user.setNom(socialMediaProfile.getName());

        } else if (SocialMediaProvider.GOOGLE.equals(socialMediaProvider)) {
            final Google google = (Google) connection.getApi();

            final String displayName = StringUtils.replaceEach(
                    google.plusOperations().getGoogleProfile().getDisplayName(), new String[] {" "},
                    new String[] {"."});
            
            // Login
            login = StringUtils.isEmpty(displayName) ? username.toLowerCase()
                    : displayName.toLowerCase();
            // Email
            email = socialMediaProfile.getEmail();

            if (google.plusOperations().getGoogleProfile().getBirthday() != null) {
	            user.setDateNaissance(
	                    new LocalDate(google.plusOperations().getGoogleProfile().getBirthday()));
            }
        }
        
        user.setLogin(login);
        user.setEmail(email);
        
        // Implicit sign up
        if (isImplicitSignUp) {
            final String password = UUID.randomUUID().toString();
        	user.setPassword(password);
            user.setEmailConfirmed(true);
        	
        	// is login unique ?
        	int i = 1;
            while (!userService.isUserLoginUnique(null, user.getLogin())) {
                user.setLogin(user.getLogin() + i);
                i++;
            }
        }
        
        logger.debug(">>>>> Social user : [{}] <<<<<", user);

        return user;
    }
}
