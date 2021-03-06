/**
 * 
 */
package fr.manu.petitesannonces.persistence.services;

import java.util.List;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author Manu
 *
 */
public interface PersistentTokenRepositoryService extends PersistentTokenRepository {

	List<PersistentRememberMeToken> listToken();
}
