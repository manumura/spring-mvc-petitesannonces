package fr.manu.petitesannonces.configuration.social;

import javax.servlet.Filter;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author Manu
 *
 */
public class CustomSpringSocialConfigurer extends SpringSocialConfigurer {

    private UserIdSource userIdSource;
	private String postLoginUrl;
	private String postFailureUrl;
	private String signupUrl;
	private String connectionAddedRedirectUrl;
	private boolean alwaysUsePostLoginUrl = false;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
		UsersConnectionRepository usersConnectionRepository = getCustomDependency(applicationContext, UsersConnectionRepository.class);
		SocialAuthenticationServiceLocator authServiceLocator = getCustomDependency(applicationContext, SocialAuthenticationServiceLocator.class);
        SocialUserDetailsService socialUsersDetailsService =
                getCustomDependency(applicationContext, SocialUserDetailsService.class);

		// Custom authentication filter
		CustomSocialAuthenticationFilter filter = new CustomSocialAuthenticationFilter(
				(AuthenticationManager) http.getSharedObject(AuthenticationManager.class),
				new AuthenticationNameUserIdSource(), usersConnectionRepository, authServiceLocator);

		RememberMeServices rememberMe = http.getSharedObject(RememberMeServices.class);
		if (rememberMe != null) {
			filter.setRememberMeServices(rememberMe);
		}

		if (this.postLoginUrl != null) {
			filter.setPostLoginUrl(this.postLoginUrl);
			filter.setAlwaysUsePostLoginUrl(this.alwaysUsePostLoginUrl);
		}

		if (this.postFailureUrl != null) {
			filter.setPostFailureUrl(this.postFailureUrl);
		}

		if (this.signupUrl != null) {
			filter.setSignupUrl(this.signupUrl);
		}

		if (this.connectionAddedRedirectUrl != null) {
			filter.setConnectionAddedRedirectUrl(this.connectionAddedRedirectUrl);
		}

		http.authenticationProvider(
                new CustomSocialAuthenticationProvider(usersConnectionRepository,
                        socialUsersDetailsService))
				.addFilterBefore((Filter) postProcess(filter), AbstractPreAuthenticatedProcessingFilter.class);
	}

	private <T> T getCustomDependency(ApplicationContext applicationContext, Class<T> dependencyType) {
		try {
			return applicationContext.getBean(dependencyType);
		} catch (NoSuchBeanDefinitionException e) {
			throw new IllegalStateException("SpringSocialConfigurer depends on " + dependencyType.getName()
					+ ". No single bean of that type found in application context.", e);
		}
	}

	@Override
	public CustomSpringSocialConfigurer userIdSource(UserIdSource userIdSource) {
		this.userIdSource = userIdSource;
		return this;
	}

	@Override
	public CustomSpringSocialConfigurer postLoginUrl(String postLoginUrl) {
		this.postLoginUrl = postLoginUrl;
		return this;
	}

	@Override
	public CustomSpringSocialConfigurer alwaysUsePostLoginUrl(boolean alwaysUsePostLoginUrl) {
		this.alwaysUsePostLoginUrl = alwaysUsePostLoginUrl;
		return this;
	}

	@Override
	public CustomSpringSocialConfigurer postFailureUrl(String postFailureUrl) {
		this.postFailureUrl = postFailureUrl;
		return this;
	}

	@Override
	public CustomSpringSocialConfigurer signupUrl(String signupUrl) {
		this.signupUrl = signupUrl;
		return this;
	}
	
	@Override
	public CustomSpringSocialConfigurer connectionAddedRedirectUrl(String connectionAddedRedirectUrl) {
		this.connectionAddedRedirectUrl = connectionAddedRedirectUrl;
		return this;
	}
}
