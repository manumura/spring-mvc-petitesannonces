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
import fr.manu.petitesannonces.persistence.dao.PasswordResetTokenDao;
import fr.manu.petitesannonces.persistence.entities.PasswordResetTokenEntity;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;

/**
 * @author emmanuel.mura
 *
 */
// TODO TU
@Repository("passwordResetTokenDao")
public class PasswordResetTokenDaoImpl extends AbstractCrudDao<Long, PasswordResetTokenEntity>
        implements PasswordResetTokenDao {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenDaoImpl.class);

    @Override
    public PasswordResetTokenEntity getByToken(final String token) throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", token);

        if (StringUtils.isEmpty(token)) {
            logger.error(
                    ">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final String sql = "FROM PasswordResetTokenEntity p WHERE p.token = :token";
            final TypedQuery<PasswordResetTokenEntity> query = super.getEntityManager().createQuery(sql, PasswordResetTokenEntity.class);
            query.setParameter("token", token);
            final PasswordResetTokenEntity entity = query.getSingleResult();

            if (entity != null) {
                UserEntity userEntity = entity.getUser();
                logger.debug("User: {}", userEntity);
            }

            logger.debug(">>>>> end : {} <<<<<", entity);
            return entity;

        } catch (NoResultException nre) {
            logger.warn("Result not found...", nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public PasswordResetTokenEntity getByUser(final UserEntity user)
            throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final Query query = super.getEntityManager()
                    .createQuery("FROM PasswordResetTokenEntity where user = :user");
            query.setParameter("user", user);

            final PasswordResetTokenEntity entity =
                    (PasswordResetTokenEntity) query.getSingleResult();

            if (entity != null) {
                UserEntity userEntity = entity.getUser();
                logger.debug("User: {}", userEntity);
            }

            logger.debug(">>>>> end : {} <<<<<", entity);
            return entity;

        } catch (NoResultException nre) {
            logger.warn("Result not found...", nre);
            return null;
        } catch (PersistenceException pe) {
            logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
            throw new DaoException(">>>>> PersistenceException <<<<<", pe);
        }
    }

    @Override
    public PasswordResetTokenEntity create(final UserEntity user, final String token)
            throws TechnicalException {

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null || StringUtils.isEmpty(token)) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
        }

        try {
            final PasswordResetTokenEntity entity =
                    new PasswordResetTokenEntity(token, user);
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

        logger.debug(">>>>> start : {} <<<<<", user);

        if (user == null || user.getId() == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return 0;
        }

        try {
            final Query query = super.getEntityManager()
                    .createQuery("DELETE PasswordResetTokenEntity where user = :user");
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
