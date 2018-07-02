package fr.manu.petitesannonces.persistence.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.persistence.dao.PersistentTokenRepositoryDao;
import fr.manu.petitesannonces.persistence.services.PersistentTokenRepositoryService;

@Service("customPersistentTokenRepository")
public class PersistentTokenRepositoryBusiness implements PersistentTokenRepositoryService {

    @Autowired
    @Qualifier("persistentTokenRepositoryDao")
    private PersistentTokenRepositoryDao persistentTokenRepositoryDao;
	
	private static final Logger logger = LoggerFactory.getLogger(PersistentTokenRepositoryBusiness.class);

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	@Override
	public void createNewToken(final PersistentRememberMeToken token) {
		
		if (token == null) {
			logger.error(">>>>> IllegalParameterException in PersistentTokenRepositoryBusiness.createNewToken <<<<<");
			return;
		}
		
		logger.debug("Creating Token for user : {}", token.getUsername());
		
		try {
            // Create new token
		    persistentTokenRepositoryDao.createNewToken(token);
			
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
			logger.error(">>>>> IllegalParameterException in PersistentTokenRepositoryBusiness.getTokenForSeries <<<<<");
			return null;
		}
		
		try {
            PersistentRememberMeToken token =
                    persistentTokenRepositoryDao.getTokenForSeries(seriesId);
            logger.debug("Token found...", token);
            return token;

		} catch (Exception e) {
			logger.error("Token not found...", e);
			return null;
		}
	}
	
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	@Override
	public void removeUserTokens(final String username) {
		
		logger.debug("Removing Token if any for user : {}", username);
		
		if (username == null || username.isEmpty()) {
            logger.error(
                    ">>>>> IllegalParameterException in PersistentTokenRepositoryBusiness.removeUserTokens <<<<<");
			return;
		}
		
		try {
            persistentTokenRepositoryDao.removeUserTokens(username);

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
            logger.error(
                    ">>>>> IllegalParameterException in PersistentTokenRepositoryBusiness.updateToken <<<<<");
			return;
		}
		
		try {
            persistentTokenRepositoryDao.updateToken(seriesId, tokenValue, lastUsed);

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
            return persistentTokenRepositoryDao.listToken();
		
		} catch (Exception e) {
			logger.error("Tokens not found...", e);
            return new ArrayList<PersistentRememberMeToken>(0);
		}
	}

}
