package fr.manu.petitesannonces.persistence.dao;

import java.util.List;

import fr.manu.petitesannonces.persistence.entities.UserRoleEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author Manu
 *
 */
public interface UserRoleDao {

    UserRoleEntity get(Long id) throws TechnicalException;;

    UserRoleEntity getByType(String type) throws TechnicalException;;
	
    List<UserRoleEntity> list() throws TechnicalException;;
}
