package fr.manu.petitesannonces.configuration.social;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

/**
 * @author Manu
 *
 */
public class CustomSocialAuthenticationFilter extends SocialAuthenticationFilter {

	private static final String DEFAULT_FAILURE_URL = "/login-failure";

	// private static final String DEFAULT_FILTER_PROCESSES_URL = "/auth";

	public CustomSocialAuthenticationFilter(AuthenticationManager authManager, UserIdSource userIdSource,
			UsersConnectionRepository usersConnectionRepository, SocialAuthenticationServiceLocator authServiceLocator) {
		super(authManager, userIdSource, usersConnectionRepository, authServiceLocator);
		// Custom failure handler
		setAuthenticationFailureHandler(new CutomSocialAuthenticationFailureHandler(
				new SimpleUrlAuthenticationFailureHandler(DEFAULT_FAILURE_URL)));
	}

}
