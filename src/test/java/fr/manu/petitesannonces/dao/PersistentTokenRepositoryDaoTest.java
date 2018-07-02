package fr.manu.petitesannonces.dao;

import java.util.Calendar;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.persistence.dao.PersistentTokenRepositoryDao;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * 
 * @author Manu
 *
 */
public class PersistentTokenRepositoryDaoTest extends TransactionalContextTest {
	
	@Autowired
    @Qualifier("persistentTokenRepositoryDao")
    private PersistentTokenRepositoryDao persistentTokenRepositoryDao;
	
	/*
	 * @see fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		final IDataSet dataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("persistent_login.xml"));
		return dataSet;
	}
	
	@Test
	public void testGet() {
		
		final Calendar cal = Calendar.getInstance();
		cal.set(2016, 7, 8, 14, 36, 45);
		cal.set(Calendar.MILLISECOND, 0);
		
        final PersistentRememberMeToken token = persistentTokenRepositoryDao.getTokenForSeries("1");
		Assert.assertNotNull(token);
		Assert.assertEquals(token.getSeries(), "1");
		Assert.assertEquals(token.getTokenValue(), "token1");
		Assert.assertEquals(token.getUsername(), "manu");
		Assert.assertEquals(token.getDate().getTime(), cal.getTime().getTime());
		
        final PersistentRememberMeToken token2 =
                persistentTokenRepositoryDao.getTokenForSeries("3");
		Assert.assertNull(token2);
	}
	
	@Test
	public void testCreateNewToken() throws BusinessException, TechnicalException {
		
		final Calendar cal = Calendar.getInstance();
		cal.set(2016, 1, 1, 23, 12, 52);
		
		// Create token
		final PersistentRememberMeToken token = new PersistentRememberMeToken("test", "3", "token3", cal.getTime());
        persistentTokenRepositoryDao.createNewToken(token);
		
        final PersistentRememberMeToken tokenCreated =
                persistentTokenRepositoryDao.getTokenForSeries("3");
		Assert.assertNotNull(tokenCreated);
		Assert.assertEquals(tokenCreated.getSeries(), "3");
		Assert.assertEquals(tokenCreated.getTokenValue(), "token3");
		Assert.assertEquals(tokenCreated.getUsername(), "test");
		Assert.assertEquals(tokenCreated.getDate().getTime(), cal.getTime().getTime());
	}
	
	@Test
	public void testUpdateNewToken() throws BusinessException, TechnicalException {
		
		final Calendar cal = Calendar.getInstance();
		cal.set(2016, 1, 1, 23, 12, 52);
		
		// Update existing token
		final PersistentRememberMeToken token2 = new PersistentRememberMeToken("manu", "666", "token4", cal.getTime());
        persistentTokenRepositoryDao.createNewToken(token2);
		
        final PersistentRememberMeToken tokenDeleted =
                persistentTokenRepositoryDao.getTokenForSeries("1");
		Assert.assertNull(tokenDeleted);
		
        final PersistentRememberMeToken tokenCreated2 =
                persistentTokenRepositoryDao.getTokenForSeries("666");
		Assert.assertNotNull(tokenCreated2);
		Assert.assertEquals(tokenCreated2.getSeries(), "666");
		Assert.assertEquals(tokenCreated2.getTokenValue(), "token4");
		Assert.assertEquals(tokenCreated2.getUsername(), "manu");
		Assert.assertEquals(tokenCreated2.getDate().getTime(), cal.getTime().getTime());
	}
	
	@Test
	public void testUpdate() throws BusinessException, TechnicalException {
		
		final Calendar cal = Calendar.getInstance();
		cal.set(2017, 11, 31, 0, 5, 12);
		cal.set(Calendar.MILLISECOND, 0);
		
        persistentTokenRepositoryDao.updateToken("1", "tokentest", cal.getTime());
        final PersistentRememberMeToken token = persistentTokenRepositoryDao.getTokenForSeries("1");
		Assert.assertNotNull(token);
		Assert.assertEquals(token.getSeries(), "1");
		Assert.assertEquals(token.getTokenValue(), "tokentest");
		Assert.assertEquals(token.getUsername(), "manu");
		Assert.assertEquals(token.getDate().getTime(), cal.getTime().getTime());
	}
	
	@Test
	public void testRemoveUserTokens() throws BusinessException, TechnicalException {
		
        persistentTokenRepositoryDao.removeUserTokens("manu");
		
        final PersistentRememberMeToken token = persistentTokenRepositoryDao.getTokenForSeries("1");
		Assert.assertNull(token);
	}
	
	@Test
	public void testList() throws BusinessException, TechnicalException {
		
        final List<PersistentRememberMeToken> tokens = persistentTokenRepositoryDao.listToken();
		
		Assert.assertNotNull(tokens);
		Assert.assertTrue(!tokens.isEmpty());
		Assert.assertTrue(tokens.size() == 2);
		
		Assert.assertEquals(tokens.get(0).getUsername(), "johnny");
		Assert.assertEquals(tokens.get(0).getSeries(), "2");
		Assert.assertEquals(tokens.get(0).getTokenValue(), "token2");
		
		Assert.assertEquals(tokens.get(1).getUsername(), "manu");
		Assert.assertEquals(tokens.get(1).getSeries(), "1");
		Assert.assertEquals(tokens.get(1).getTokenValue(), "token1");
	}
	
}
