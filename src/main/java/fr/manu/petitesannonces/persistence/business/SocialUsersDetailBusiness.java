package fr.manu.petitesannonces.persistence.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.dto.SocialUser;
import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;

@Service("customSocialUsersDetailService")
public class SocialUsersDetailBusiness implements SocialUserDetailsService {

	@Autowired
	@Qualifier("userService")
    private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(SocialUsersDetailBusiness.class);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public SocialUserDetails loadUserByUserId(final String userId) {
    	try {
            // userId = login
	        final User user = userService.getByLogin(userId);

            if (user == null) {
                logger.error(">>>>> User not found for ID [{}] <<<<<", userId);
                return null;
            }

	        final SocialUser socialUser = new SocialUser(user);
	        logger.debug(">>>>> Social user : {} <<<<<", socialUser);
	        
            return socialUser;

            // return new org.springframework.social.security.SocialUser(socialUser.getUsername(),
            // socialUser.getPassword(), socialUser.isEnabled(), accountNonExpired,
            // credentialsNonExpired,
            // accountNonLocked, socialUser.getAuthorities());

        } catch (BusinessException | TechnicalException e) {
            logger.error(">>>>> Exception : {} <<<<<", e.getMessage(), e);
            throw new UsernameNotFoundException(
                    ">>>>> UsernameNotFoundException : " + e.getMessage() + " <<<<<", e);
		}
    }

}