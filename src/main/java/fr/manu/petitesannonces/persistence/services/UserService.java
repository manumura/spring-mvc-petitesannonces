package fr.manu.petitesannonces.persistence.services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author Manu
 *
 */
public interface UserService {
	
	List<User> list() throws BusinessException, TechnicalException;
	
	@PreAuthorize("hasRole('ADMIN')")
	User markAsDeleted(Long id) throws BusinessException, TechnicalException;
	
	@PreAuthorize("hasRole('ADMIN')")
    void delete(Long id) throws BusinessException, TechnicalException;
	
	User get(Long id) throws BusinessException, TechnicalException;
	
	User getByLogin(String login) throws BusinessException, TechnicalException;
	
	User getByEmail(String email) throws BusinessException, TechnicalException;
	
	User getBySignInProviderUserId(String signInProviderUserId) throws BusinessException, TechnicalException;

	User create(User user) throws BusinessException, TechnicalException;

	User update(User user) throws BusinessException, TechnicalException;
	
	boolean isUserLoginUnique(Long id, String login) throws BusinessException, TechnicalException;
	
	boolean isUserEmailUnique(Long id, String email) throws BusinessException, TechnicalException;
}
