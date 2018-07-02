package fr.manu.petitesannonces.configuration.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.util.Assert;

import fr.manu.petitesannonces.dto.SocialUser;
import fr.manu.petitesannonces.persistence.business.SocialUsersDetailBusiness;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.web.exceptions.CustomDisabledException;

/**
 * @author Manu
 *
 */
public class CustomSocialAuthenticationProvider extends SocialAuthenticationProvider {

    @Autowired
    @Qualifier("converter")
    private Converter converter;
	
	private SocialUserDetailsService customUserDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(SocialUsersDetailBusiness.class);

	public CustomSocialAuthenticationProvider(UsersConnectionRepository usersConnectionRepository,
			SocialUserDetailsService userDetailsService) {
		super(usersConnectionRepository, userDetailsService);
		this.customUserDetailsService = userDetailsService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(SocialAuthenticationToken.class, authentication, "unsupported authentication type");
		Assert.isTrue(!authentication.isAuthenticated(), "already authenticated");
		SocialAuthenticationToken authToken = (SocialAuthenticationToken) authentication;
		String providerId = authToken.getProviderId();
		Connection<?> connection = authToken.getConnection();

		String userId = toUserId(connection);
		if (userId == null) {
			throw new BadCredentialsException("Unknown access token");
		}

        SocialUser socialUser = (SocialUser) customUserDetailsService.loadUserByUserId(userId);
        if (socialUser == null) {
            logger.error(">>>>> User is disabled <<<<<");
            throw new UsernameNotFoundException("Social user not found");
		}
		
        logger.debug(">>>>> Social user : {} <<<<<", socialUser);
        
        // email confirmed ?
        if (!socialUser.isEnabled()) {
        	logger.error(">>>>> User is disabled <<<<<");
            throw new CustomDisabledException(">>>>> User is disabled <<<<<", socialUser.getUser());
        }
        
        if (!socialUser.isAccountNonExpired()) {
        	logger.error(">>>>> Account is expired <<<<<");
        	throw new AccountExpiredException(">>>>> Account is expired <<<<<");
        }
        
        if (!socialUser.isCredentialsNonExpired()) {
			logger.error(">>>>> Credentials expired <<<<<");
			throw new CredentialsExpiredException(">>>>> Credentials expired <<<<<");
		}
		
        if (!socialUser.isAccountNonLocked()) {
			logger.error(">>>>> Account is locked <<<<<");
			throw new LockedException(">>>>> Account is locked <<<<<");
		}

        return new SocialAuthenticationToken(connection, socialUser,
                authToken.getProviderAccountData(), getAuthorities(providerId, socialUser));
	}

}
