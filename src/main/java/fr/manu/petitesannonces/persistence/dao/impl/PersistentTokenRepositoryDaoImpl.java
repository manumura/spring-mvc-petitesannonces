package fr.manu.petitesannonces.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.persistence.dao.AbstractCrudDao;
import fr.manu.petitesannonces.persistence.dao.PersistentTokenRepositoryDao;
import fr.manu.petitesannonces.persistence.entities.PersistentLoginEntity;

@Repository("persistentTokenRepositoryDao")
public class PersistentTokenRepositoryDaoImpl extends AbstractCrudDao<String, PersistentLoginEntity>
        implements PersistentTokenRepositoryDao {
	
	private static final Logger logger = LoggerFactory.getLogger(PersistentTokenRepositoryDaoImpl.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void createNewToken(final PersistentRememberMeToken token) {
		
		if (token == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
			return;
		}
		
		logger.debug("Creating Token for user : {}", token.getUsername());
		
		try {
            // Remove existing tokens for user if any
            this.removeUserTokens(token.getUsername());

            // Flush session
            super.getEntityManager().flush();

			// Create new token
			final PersistentLoginEntity persistentLogin = new PersistentLoginEntity();
			persistentLogin.setUsername(token.getUsername());
			persistentLogin.setSeries(token.getSeries());
			persistentLogin.setToken(token.getTokenValue());
			persistentLogin.setLastUsed(token.getDate());
			super.persist(persistentLogin);
			
		} catch (Exception e) {
			logger.error("Token not created...", e);
			return;
		}
	}

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
		
		logger.debug("Fetch Token if any for seriesId : {}", seriesId);
		
		if (seriesId == null || seriesId.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
			return null;
		}
		
		try {
			final String sql = "FROM PersistentLoginEntity p WHERE p.series = :seriesId";
            final TypedQuery<PersistentLoginEntity> query = super.getEntityManager().createQuery(sql, PersistentLoginEntity.class);
            query.setParameter("seriesId", seriesId);
            final PersistentLoginEntity persistentLogin = query.getSingleResult();
            
			PersistentRememberMeToken token = null;
			if (persistentLogin != null) {
				token = new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
					persistentLogin.getToken(), persistentLogin.getLastUsed());
			}
			
			return token;
			
        } catch (NoResultException nre) {
            logger.warn("Token not found...", nre);
            return null;
		} catch (NonUniqueResultException nure) {
			logger.error("Token not unique...", nure);
			return null;
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
			return null;
		}
	}
	
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void removeUserTokens(final String username) {
		
		logger.debug("Removing Token if any for user : {}", username);
		
		if (username == null || username.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
			return;
		}
		
		try {
			final String sql = "FROM PersistentLoginEntity p WHERE p.username = :username";
            final TypedQuery<PersistentLoginEntity> query = super.getEntityManager().createQuery(sql, PersistentLoginEntity.class);
            query.setParameter("username", username);
            final List<PersistentLoginEntity> persistentLoginList = query.getResultList();
            
            // TODO : IN
			if (persistentLoginList != null && !persistentLoginList.isEmpty()) {
				logger.debug("rememberMe was selected");
				for (final PersistentLoginEntity persistentLogin : persistentLoginList) {
					super.delete(persistentLogin);
				}
			}

		} catch (Exception e) {
			logger.error("Token not removed...", e);
			return;
		}
	}

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	@Override
	public void updateToken(final String seriesId, final String tokenValue, final Date lastUsed) {
		
		logger.debug("Updating Token for seriesId : {}", seriesId);
		
		if (seriesId == null || seriesId.isEmpty() || tokenValue == null || tokenValue.isEmpty() || lastUsed == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
			return;
		}
		
		try {
			final PersistentLoginEntity persistentLogin = super.getByKey(seriesId);
			persistentLogin.setToken(tokenValue);
			persistentLogin.setLastUsed(lastUsed);
			super.merge(persistentLogin);
		} catch (Exception e) {
			logger.error("Token not updated...", e);
			return;
		}
	}
	
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<PersistentRememberMeToken> listToken() {
		
		logger.debug("List all Tokens");
		
		try {
            final List<PersistentRememberMeToken> tokens = new ArrayList<>();
            
            final String sql = "FROM PersistentLoginEntity p ORDER BY p.username ASC";
            final TypedQuery<PersistentLoginEntity> query = super.getEntityManager().createQuery(sql, PersistentLoginEntity.class);
            final List<PersistentLoginEntity> results = query.getResultList();
			
			for (final PersistentLoginEntity dao : results) {
				if (dao != null) {
					final PersistentRememberMeToken token = new PersistentRememberMeToken(dao.getUsername(), dao.getSeries(), dao.getToken(), dao.getLastUsed());
					tokens.add(token);
				}
			}
			
			return tokens;
		
		} catch (Exception e) {
			logger.error("Tokens not found...", e);
            return new ArrayList<>(0);
		}
	}

}
