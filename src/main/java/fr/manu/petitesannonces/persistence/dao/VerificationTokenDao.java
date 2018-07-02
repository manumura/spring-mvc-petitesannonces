package fr.manu.petitesannonces.persistence.dao;

import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.entities.VerificationTokenEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author emmanuel.mura
 *
 */
public interface VerificationTokenDao {

    VerificationTokenEntity getByToken(String token) throws TechnicalException;

    VerificationTokenEntity getByUser(UserEntity user) throws TechnicalException;

    VerificationTokenEntity create(UserEntity user, String token) throws TechnicalException;

    int delete(final UserEntity user) throws TechnicalException;
}
