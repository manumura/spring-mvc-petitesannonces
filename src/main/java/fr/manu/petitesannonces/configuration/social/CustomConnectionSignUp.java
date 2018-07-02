/**
 * 
 */
package fr.manu.petitesannonces.configuration.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.web.security.SocialUserFactory;

/**
 * @author Manu
 * Not used for now
 *
 */
@Component
public class CustomConnectionSignUp implements ConnectionSignUp {

    private static final Logger logger = LoggerFactory.getLogger(CustomConnectionSignUp.class);

	@Autowired
	@Qualifier("userService")
	private UserService userService;

    @Autowired
    private SocialUserFactory socialUserFactory;

	@Override
	public String execute(Connection<?> connection) {

        logger.debug(">>>>> start - connection : {} <<<<<", connection);

		try {
            final User user = socialUserFactory.getUserFromSocialProviderConnection(connection);
			final User userCreated = userService.create(user);
            logger.debug("Created user: {}", userCreated);

            // userId = login
            return userCreated.getLogin();

		} catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> Exception in CustomConnectionSignUp.execute : {} <<<<<",
                    e.getMessage(), e);
			return null;
		}
	}

}
