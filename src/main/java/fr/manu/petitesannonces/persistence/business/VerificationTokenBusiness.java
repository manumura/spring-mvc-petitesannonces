package fr.manu.petitesannonces.persistence.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.VerificationToken;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.dao.VerificationTokenDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;
import fr.manu.petitesannonces.persistence.exceptions.impl.ServiceException;
import fr.manu.petitesannonces.persistence.services.VerificationTokenService;

/**
 * @author emmanuel.mura
 *
 */
@Service("verificationTokenService")
public class VerificationTokenBusiness implements VerificationTokenService {

    @Autowired
    @Qualifier("verificationTokenDao")
    private VerificationTokenDao verificationTokenDao;

    @Autowired
    @Qualifier("converter")
    private Converter converter;

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenBusiness.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.manu.petitesannonces.persistence.services.VerificationTokenService#getByToken(java.lang.
     * String)
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public VerificationToken getByToken(final String token)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", token);

        if (StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final VerificationToken verificationToken = converter.convert(
                    verificationTokenDao.getByToken(token),
                    VerificationToken.class);

            logger.debug(">>>>> end : {} <<<<<", verificationToken);
            return verificationToken;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.manu.petitesannonces.persistence.services.VerificationTokenService#getByUser(fr.manu.
     * petitesannonces.dto.User)
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public VerificationToken getByUser(final User user) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final VerificationToken verificationToken = converter
                    .convert(
                            verificationTokenDao
                                    .getByUser(converter.convert(user, UserEntity.class)),
                            VerificationToken.class);

            logger.debug(">>>>> end : {} <<<<<", verificationToken);
            return verificationToken;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.manu.petitesannonces.persistence.services.VerificationTokenService#create(fr.manu.
     * petitesannonces.dto.User, java.lang.String)
     */
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    @Override
    public VerificationToken create(final User user, final String token)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null || StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            // Delete existing token(s) for user
            int count = verificationTokenDao.delete(converter.convert(user, UserEntity.class));
            logger.debug(">>>>> {} token(s) deleted for user <<<<<", count);

            final VerificationToken verificationTokenCreated =
                    converter
                            .convert(
                                    verificationTokenDao.create(
                                            converter.convert(user, UserEntity.class), token),
                            VerificationToken.class);

            logger.debug(">>>>> end : {} <<<<<",
                    verificationTokenCreated);

            return verificationTokenCreated;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

}
