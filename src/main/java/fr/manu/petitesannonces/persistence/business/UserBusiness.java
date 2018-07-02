/**
 * 
 */
package fr.manu.petitesannonces.persistence.business;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.enums.UserRoleType;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.dao.UserDao;
import fr.manu.petitesannonces.persistence.dao.UserRoleDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.entities.UserRoleEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;
import fr.manu.petitesannonces.persistence.exceptions.impl.IllegalParameterException;
import fr.manu.petitesannonces.persistence.exceptions.impl.ServiceException;
import fr.manu.petitesannonces.persistence.services.UserService;

/**
 * @author Manu
 *
 */
@Service("userService")
public class UserBusiness implements UserService {

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
    @Qualifier("userRoleDao")
    private UserRoleDao userRoleDao;

    @Autowired
    @Qualifier("converter")
    private Converter converter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserBusiness.class);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<User> list() throws BusinessException, TechnicalException {

        logger.debug(">>>>> start <<<<<");

        try {
            List<UserEntity> users = userDao.list();
            logger.debug(">>>>> end <<<<<");
            return converter.convertList(users, User.class);

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public User get(final Long id) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", id);

        if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final User user = converter.convert(userDao.get(id), User.class);
            logger.debug(">>>>> end : {} <<<<<", user);
            return user;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLoginUnique(final Long id, final String login)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} - {} <<<<<", id, login);

        if (login == null || login.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            throw new IllegalParameterException(">>>>> Illegal parameter <<<<<");
        }

        final User user = this.getByLogin(login);
        final boolean result = user == null || (id != null && user.getId().equals(id));
        logger.debug(">>>>> end : {} <<<<<", result);
        return result;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserEmailUnique(final Long id, final String email)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> UserBusiness.isUserEmailUnique - start : {} - {} <<<<<", id, email);

        if (email == null || email.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            throw new IllegalParameterException(">>>>> Illegal parameter <<<<<");
        }

        final User user = this.getByEmail(email);
        final boolean result = user == null || (id != null && user.getId().equals(id));
        logger.debug(">>>>> end : {} <<<<<", result);
        return result;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public User getByLogin(final String login) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", login);

        if (login == null || login.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final User user = converter.convert(userDao.getByLogin(login), User.class);
            logger.debug(">>>>> end : {} <<<<<", user);
            return user;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public User getByEmail(final String email) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", email);

        if (email == null || email.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final User user = converter.convert(userDao.getByEmail(email), User.class);
            logger.debug(">>>>> end : {} <<<<<", user);
            return user;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public User getBySignInProviderUserId(final String signInProviderUserId)
            throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", signInProviderUserId);

        if (signInProviderUserId == null || signInProviderUserId.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final User user = converter
                    .convert(userDao.getBySignInProviderUserId(signInProviderUserId), User.class);
            logger.debug(">>>>> end : {} <<<<<", user);
            return user;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User markAsDeleted(final Long id) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", id);

        if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final UserEntity entity = userDao.get(id);
            entity.setDeleted(true);
            final User userUpdated = converter.convert(userDao.update(entity), User.class);
            logger.debug(">>>>> end : {} <<<<<", userUpdated);
            return userUpdated;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void delete(final Long id) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", id);

        if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return;
        }

        try {
            userDao.remove(id);
            logger.debug(">>>>> end : {} <<<<<", id);

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User create(final User user) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final UserRoleEntity userRole = converter.convert(
                    userRoleDao.getByType(UserRoleType.USER.getUserProfileType()),
                    UserRoleEntity.class);
            Set<UserRoleEntity> userRoles = new HashSet<UserRoleEntity>(Arrays.asList(userRole));

            // Set default values
            final UserEntity entity = converter.convert(user, UserEntity.class);
            entity.setPassword(passwordEncoder.encode(user.getPassword()));
            entity.setDateInscription(LocalDateTime.now());
            entity.setDeleted(false);
            entity.setUserRoles(userRoles);
            userDao.create(entity);
            final User userCreated = converter.convert(entity, User.class);

            logger.debug(">>>>> end : {} <<<<<", userCreated);
            return userCreated;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User update(final User user) throws BusinessException, TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final UserEntity userCurrent = userDao.get(user.getId());
            final UserEntity userToUpdate = converter.convert(user, userCurrent);
            // userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));

            final User userUpdated = converter.convert(userDao.update(userToUpdate), User.class);
            logger.debug(">>>>> end : {} <<<<<", userUpdated);

            return userUpdated;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException <<<<<", de);
        }
    }

}
