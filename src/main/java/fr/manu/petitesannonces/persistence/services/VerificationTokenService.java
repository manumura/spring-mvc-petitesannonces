package fr.manu.petitesannonces.persistence.services;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.VerificationToken;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * @author emmanuel.mura
 *
 */
public interface VerificationTokenService {

    VerificationToken getByToken(String token) throws BusinessException, TechnicalException;

    VerificationToken getByUser(User user) throws BusinessException, TechnicalException;

    VerificationToken create(User user, String token) throws BusinessException, TechnicalException;
}
