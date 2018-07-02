/**
 * 
 */
package fr.manu.petitesannonces.persistence.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.manu.petitesannonces.persistence.dao.AbstractCrudDao;
import fr.manu.petitesannonces.persistence.dao.UserDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;

/**
 * @author Manu
 *
 */
@Repository("userDao")
public class UserDaoImpl extends AbstractCrudDao<Long, UserEntity> implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public List<UserEntity> list() throws TechnicalException {

        logger.debug(">>>>> start <<<<<");

        try {
        	 final String sql = "FROM UserEntity u ORDER BY u.login ASC";
             final TypedQuery<UserEntity> query = super.getEntityManager().createQuery(sql, UserEntity.class);
             final List<UserEntity> results = query.getResultList();
        	
            logger.debug(">>>>> end : {} <<<<<", results);

            return results;

        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity get(final Long id) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", id);

        if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final UserEntity user = super.getByKey(id);

            if (user == null) {
                logger.error(">>>>> User not found for id : {} <<<<<", id);
                return null;
            }

            // Lazy init
            user.getUserRoles().size();
            // Hibernate.initialize(user.getUserRoles());

            logger.debug(">>>>> end : {} <<<<<", user);

            return user;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage()
                    + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity getByLogin(final String login) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", login);

        if (login == null || login.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
        	final TypedQuery<UserEntity> query = super.getEntityManager()
                    .createQuery("FROM UserEntity u where u.login = :login", UserEntity.class);
            query.setParameter("login", login);
            final UserEntity user = query.getSingleResult();
            
            if (user != null) {
                // Lazy init
                user.getUserRoles().size();
                // Hibernate.initialize(user.getUserRoles());
            }

            logger.debug(">>>>> end : {} <<<<<", user);

            return user;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<",
                    pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity getByEmail(final String email) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", email);

        if (email == null || email.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
        	final TypedQuery<UserEntity> query = super.getEntityManager()
                    .createQuery("FROM UserEntity u where u.email = :email", UserEntity.class);
            query.setParameter("email", email);
            final UserEntity user = query.getSingleResult();
            
            if (user != null) {
                // Lazy init
                user.getUserRoles().size();
                // Hibernate.initialize(user.getUserRoles());
            }

            logger.debug(">>>>> end : {} <<<<<", user);

            return user;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<", pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity getBySignInProviderUserId(final String signInProviderUserId)
            throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", signInProviderUserId);

        if (signInProviderUserId == null || signInProviderUserId.isEmpty()) {
        	logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
        	final TypedQuery<UserEntity> query = super.getEntityManager()
                    .createQuery("FROM UserEntity u where u.signInProviderUserId = :signInProviderUserId", UserEntity.class);
            query.setParameter("signInProviderUserId", signInProviderUserId);
            final UserEntity user = query.getSingleResult();
            
            if (user != null) {
                // Lazy init
                user.getUserRoles().size();
                // Hibernate.initialize(user.getUserRoles());
            }

            logger.debug(">>>>> end : " + user + " <<<<<");

            return user;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<", pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public void remove(final Long id) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", id);

        if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return;
        }

        try {
            super.delete(super.getByKey(id));
            logger.debug(">>>>> end : {} <<<<<", id);
            
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<", pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity create(final UserEntity user) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            super.persist(user);
            logger.debug(">>>>> end : {} <<<<<", user);

            return user;

        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<", pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public UserEntity update(final UserEntity user) throws TechnicalException {

        logger.debug(">>>>> start : " + user + " <<<<<");

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final UserEntity userUpdated = super.merge(user);
            logger.debug(">>>>> end : {} <<<<<", userUpdated);
            return userUpdated;

        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : {} <<<<<", pe.getMessage(), pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

}
