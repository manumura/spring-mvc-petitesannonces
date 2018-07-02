/**
 * 
 */
package fr.manu.petitesannonces.persistence.dao;

import java.util.List;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author Manu
 *
 */
public interface PersistentTokenRepositoryDao extends PersistentTokenRepository {

	List<PersistentRememberMeToken> listToken();
}
