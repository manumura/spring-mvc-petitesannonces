
/**
 * 
 */
package fr.manu.petitesannonces.persistence.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.manu.petitesannonces.persistence.dao.AbstractCrudDao;
import fr.manu.petitesannonces.persistence.dao.UserRoleDao;
import fr.manu.petitesannonces.persistence.entities.UserRoleEntity;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;

/**
 * @author Manu
 *
 */
@Repository("userRoleDao")
public class UserRoleDaoImpl extends AbstractCrudDao<Long, UserRoleEntity> implements UserRoleDao {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleDaoImpl.class);

	@Override
	public UserRoleEntity get(final Long id) throws TechnicalException {

		logger.debug(">>>>> start : {} <<<<<", id);

		if (id == null) {
			logger.error(">>>>> Illegal parameter <<<<<");
			return null;
		}

		try {
			final UserRoleEntity role = super.getByKey(id);
			logger.debug(">>>>> end : {} <<<<<", role);
			return role;

		} catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
		} catch (PersistenceException pe) {
			logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
			throw new DaoException(">>>>> PersistenceException <<<<<", pe);
		}
	}

	@Override
	public UserRoleEntity getByType(final String type) throws TechnicalException {

		logger.debug(">>>>> start : {} <<<<<", type);

		if (type == null || type.isEmpty()) {
			logger.error(">>>>> Illegal parameter <<<<<");
			return null;
		}

		try {
			final String sql = "FROM UserRoleEntity u WHERE u.type = :type";
			final TypedQuery<UserRoleEntity> query = super.getEntityManager().createQuery(sql, UserRoleEntity.class);
			query.setParameter("type", type);
			final UserRoleEntity role = query.getSingleResult();
			logger.debug(">>>>> end : {} <<<<<", role);
			return role;

		} catch (NoResultException nre) {
            logger.warn(">>>>> NoResultException : {} <<<<<", nre.getMessage(), nre);
            return null;
		} catch (PersistenceException pe) {
			logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
			throw new DaoException(">>>>> PersistenceException <<<<<", pe);
		}
	}

	@Override
	public List<UserRoleEntity> list() throws TechnicalException {

		logger.debug(">>>>> start <<<<<");

		try {
			final String sql = "FROM UserRoleEntity u ORDER BY u.type ASC";
			final TypedQuery<UserRoleEntity> query = super.getEntityManager().createQuery(sql, UserRoleEntity.class);
			final List<UserRoleEntity> listUserRole = query.getResultList();
			logger.debug(">>>>> end : {} <<<<<", listUserRole);
			return listUserRole;

		} catch (PersistenceException pe) {
			logger.error(">>>>> PersistenceException : " + pe.getMessage() + " <<<<<", pe);
			throw new DaoException(">>>>> PersistenceException <<<<<", pe);
		}
	}
}
