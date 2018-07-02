package fr.manu.petitesannonces.persistence.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.manu.petitesannonces.persistence.dao.AbstractCrudDao;
import fr.manu.petitesannonces.persistence.dao.VerificationTokenDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.entities.VerificationTokenEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;

/**
 * @author emmanuel.mura
 *
 */
@Repository("verificationTokenDao")
public class VerificationTokenDaoImpl extends AbstractCrudDao<Long, VerificationTokenEntity>
        implements VerificationTokenDao {

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenDaoImpl.class);

    @Override
    public VerificationTokenEntity getByToken(final String token)
            throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", token);

        if (StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
        	final TypedQuery<VerificationTokenEntity> query = super.getEntityManager()
                    .createQuery("FROM VerificationTokenEntity t WHERE t.token = :token", VerificationTokenEntity.class);
            query.setParameter("token", token);
            final VerificationTokenEntity entity = query.getSingleResult();
            
            if (entity != null) {
                // Lazy init
                UserEntity userEntity = entity.getUser();
                logger.debug("User: {}", userEntity);
            }

            logger.debug(">>>>> end : {} <<<<<", entity);
            return entity;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public VerificationTokenEntity getByUser(final UserEntity user) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final TypedQuery<VerificationTokenEntity> query = super.getEntityManager()
                    .createQuery("FROM VerificationTokenEntity t where t.user = :user", VerificationTokenEntity.class);
            query.setParameter("user", user);
            final VerificationTokenEntity entity = query.getSingleResult();

            if (entity != null) {
                // Lazy init
                UserEntity userEntity = entity.getUser();
                logger.debug("User: {}", userEntity);
            }

            logger.debug(">>>>> VerificationTokenBusiness.getByUser - end : {} <<<<<", entity);
            return entity;

        } catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public VerificationTokenEntity create(final UserEntity user, final String token)
            throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null || StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            // Delete existing token(s) for user
            int count = this.delete(user);
            logger.debug(">>>>> {} token(s) deleted for user <<<<<", count);

            final VerificationTokenEntity entity =
                    new VerificationTokenEntity(token, user);
            super.persist(entity);

            logger.debug(">>>>> end : {} <<<<<", entity);
            return entity;

        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public int delete(final UserEntity user) throws TechnicalException {

        logger.debug(">>>>> start : " + user + " <<<<<");

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return 0;
        }

        try {
            final Query query = super.getEntityManager()
                    .createQuery("DELETE VerificationTokenEntity where user = :user");
            query.setParameter("user", user);
            int count = query.executeUpdate();

            logger.debug(">>>>> end : {} record(s) deleted <<<<<", count);
            return count;

        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }
}
