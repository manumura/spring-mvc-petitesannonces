package fr.manu.petitesannonces.persistence.dao;

import fr.manu.petitesannonces.persistence.entities.PasswordResetTokenEntity;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author emmanuel.mura
 *
 */
public interface PasswordResetTokenDao {

    PasswordResetTokenEntity getByToken(String token) throws TechnicalException;

    PasswordResetTokenEntity getByUser(UserEntity user) throws TechnicalException;

    PasswordResetTokenEntity create(UserEntity user, String token) throws TechnicalException;

    int delete(final UserEntity user) throws TechnicalException;
}
