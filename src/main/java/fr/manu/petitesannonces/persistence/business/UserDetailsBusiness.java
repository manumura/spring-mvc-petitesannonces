package fr.manu.petitesannonces.persistence.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.UserRole;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;

@Service("customUserDetailsService")
public class UserDetailsBusiness implements UserDetailsService {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsBusiness.class);
	
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public UserDetails loadUserByUsername(final String login) {
		
        logger.debug(">>>>> start : {} <<<<<", login);
		
		if (login == null || login.isEmpty()) {
            logger.error(">>>>> Login is empty <<<<<");
            throw new UsernameNotFoundException(">>>>> Login is empty <<<<<");
		}
		
		try {
			final User user = userService.getByLogin(login);
			
			logger.debug("User : {}", user);
			
			if (user == null){
                logger.error("User not found for login : {}", login);
                throw new UsernameNotFoundException(">>>>> Username not found <<<<<");
			}
			
            // boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;

            final org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(user.getLogin(),
                            user.getPassword(), user.getEmailConfirmed(), accountNonExpired,
                            credentialsNonExpired,
                            accountNonLocked,
                            getGrantedAuthorities(user));
            logger.debug(">>>>> end : {} <<<<<", userDetails);
			return userDetails;
			
		} catch (BusinessException | TechnicalException e) {
			logger.error(">>>>> Exception : {} <<<<<", e.getMessage(), e);
			throw new UsernameNotFoundException(">>>>> An exception occured <<<<<", e);
        }
	}
	
	/**
	 * getGrantedAuthorities
	 * 
	 * @param user the user
	 * @return list of granted authorities
	 */
	private List<GrantedAuthority> getGrantedAuthorities(final User user){
		
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (UserRole userRole : user.getUserRoles()){
			logger.debug("UserRole : {}", userRole);
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getType()));
		}
		
        logger.debug("Authorities : {}", authorities);
		return authorities;
	}
}
