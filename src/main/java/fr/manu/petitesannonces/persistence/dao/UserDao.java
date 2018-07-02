package fr.manu.petitesannonces.persistence.dao;

import java.util.List;

import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author Manu
 *
 */
public interface UserDao {
	
    List<UserEntity> list() throws TechnicalException;
	
    void remove(Long id) throws TechnicalException;
	
    UserEntity get(Long id) throws TechnicalException;
	
    UserEntity getByLogin(String login) throws TechnicalException;
	
    UserEntity getByEmail(String email) throws TechnicalException;
	
    UserEntity getBySignInProviderUserId(String signInProviderUserId) throws TechnicalException;

    UserEntity create(UserEntity user) throws TechnicalException;

    UserEntity update(UserEntity user) throws TechnicalException;
}
