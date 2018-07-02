package fr.manu.petitesannonces.configuration.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;

public class CustomUserIdSource implements UserIdSource {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomUserIdSource.class);
	
	@Override
	public String getUserId() {
		
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
            logger.error(">>>>> Authentication failed <<<<<");
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		
        logger.debug(">>>>> user ID : {} <<<<<", authentication.getName());
        return authentication.getName();
	}
}
