package fr.manu.petitesannonces.persistence.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fr.manu.petitesannonces.dto.PasswordResetToken;
import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.dao.PasswordResetTokenDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;
import fr.manu.petitesannonces.persistence.exceptions.impl.ServiceException;
import fr.manu.petitesannonces.persistence.services.PasswordResetTokenService;

/**
 * @author emmanuel.mura
 *
 */
// TODO TU
@Service("passwordResetTokenService")
public class PasswordResetTokenBusiness implements PasswordResetTokenService {

    @Autowired
    @Qualifier("passwordResetTokenDao")
    private PasswordResetTokenDao passwordResetTokenDao;

    @Autowired
    @Qualifier("converter")
    private Converter converter;

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenBusiness.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.manu.petitesannonces.persistence.services.PasswordResetTokenService#getByToken(java.lang.
     * String)
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public PasswordResetToken getByToken(final String token)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", token);

        if (StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final PasswordResetToken passwordResetToken = converter.convert(
                    passwordResetTokenDao.getByToken(token),
                    PasswordResetToken.class);

            logger.debug(">>>>> end : {} <<<<<",
                    passwordResetToken);
            return passwordResetToken;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.manu.petitesannonces.persistence.services.PasswordResetTokenService#getByUser(fr.manu.
     * petitesannonces.dto.User)
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public PasswordResetToken getByUser(final User user)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final PasswordResetToken passwordResetToken =
                    converter.convert(passwordResetTokenDao.getByUser(converter.convert(user,
                            UserEntity.class)), PasswordResetToken.class);

            logger.debug(">>>>> end : {} <<<<<",
                    passwordResetToken);
            return passwordResetToken;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.manu.petitesannonces.persistence.services.PasswordResetTokenService#create(fr.manu.
     * petitesannonces.dto.User, java.lang.String)
     */
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    @Override
    public PasswordResetToken create(final User user, final String token)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null || StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            // Delete existing token(s) for user
            int count = passwordResetTokenDao.delete(converter.convert(user, UserEntity.class));
            logger.debug(">>>>> {} token(s) deleted for user <<<<<", count);

            final PasswordResetToken passwordResetTokenCreated =
                    converter
                            .convert(
                                    passwordResetTokenDao.create(
                                            converter.convert(user, UserEntity.class), token),
                                    PasswordResetToken.class);

            logger.debug(">>>>> end : {} <<<<<", passwordResetTokenCreated);

            return passwordResetTokenCreated;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

}
