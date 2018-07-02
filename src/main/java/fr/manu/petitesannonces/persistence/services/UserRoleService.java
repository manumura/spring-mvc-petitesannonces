package fr.manu.petitesannonces.persistence.services;

import java.util.List;

import fr.manu.petitesannonces.dto.UserRole;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author Manu
 *
 */
public interface UserRoleService {

	UserRole get(Long id) throws BusinessException, TechnicalException;;

	UserRole getByType(String type) throws BusinessException, TechnicalException;;
	
	List<UserRole> list() throws BusinessException, TechnicalException;;
}
