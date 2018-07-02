package fr.manu.petitesannonces.persistence.services;

import fr.manu.petitesannonces.dto.PasswordResetToken;
import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author emmanuel.mura
 *
 */
public interface PasswordResetTokenService {

    PasswordResetToken getByToken(String token) throws BusinessException, TechnicalException;

    PasswordResetToken getByUser(User user) throws BusinessException, TechnicalException;

    PasswordResetToken create(User user, String token) throws BusinessException, TechnicalException;
}
