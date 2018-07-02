package fr.manu.petitesannonces.dao;

import java.util.Calendar;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.persistence.dao.UserDao;
import fr.manu.petitesannonces.persistence.dao.VerificationTokenDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.entities.VerificationTokenEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * 
 * @author Manu
 *
 */
public class VerificationTokenDaoTest extends TransactionalContextTest {
	
	@Autowired
    @Qualifier("verificationTokenDao")
    private VerificationTokenDao verificationTokenDao;
	
	@Autowired
    @Qualifier("userDao")
    private UserDao userDao;
	
	/*
	 * @see fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		final IDataSet[] datasets = new IDataSet[] {
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account.xml")),
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account_verification_token.xml")),
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_role.xml")),
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account_user_role.xml"))
		};
		return new CompositeDataSet(datasets);
	}
	
	@Test
	public void testGetByToken() throws BusinessException, TechnicalException {
        final VerificationTokenEntity result =
                verificationTokenDao.getByToken("7a0ba0b6-9b30-4493-997a-6ae1a5f6ad85");
		Assert.assertNotNull(result);
		Assert.assertEquals(1l, result.getId().longValue());
		Assert.assertEquals("7a0ba0b6-9b30-4493-997a-6ae1a5f6ad85", result.getToken());
		Assert.assertEquals(1l, result.getUser().getId().longValue());
		Assert.assertEquals("manu", result.getUser().getLogin());
		
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 0, 10, 20, 22, 13);
		cal.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(new LocalDateTime(cal.getTimeInMillis()), result.getExpiryDate());
	}

	@Test
    public void testCreateGetDelete() throws BusinessException, TechnicalException {
        // final UserEntity user = userDao.create(getSampleUser());
        final UserEntity user = userDao.get(1l);
		
        final VerificationTokenEntity resultCreate =
                verificationTokenDao.create(user, "111-222-333");
		Assert.assertNotNull(resultCreate);
        // Assert.assertEquals(3l, resultCreate.getId().longValue());
        Assert.assertNotNull(resultCreate.getId());
		Assert.assertEquals("111-222-333", resultCreate.getToken());
//		Assert.assertEquals(3l, resultCreate.getUser().getId().longValue());
        Assert.assertEquals("manu", resultCreate.getUser().getLogin());
		Assert.assertNotNull(resultCreate.getExpiryDate());
		
        final VerificationTokenEntity resultGet = verificationTokenDao.getByUser(user);
		Assert.assertNotNull(resultGet);
        // Assert.assertEquals(3l, resultGet.getId().longValue());
        Assert.assertNotNull(resultGet.getId());
		Assert.assertEquals("111-222-333", resultGet.getToken());
//		Assert.assertEquals(3l, resultGet.getUser().getId().longValue());
        Assert.assertEquals("manu", resultGet.getUser().getLogin());
		Assert.assertNotNull(resultGet.getExpiryDate());

        final int count = verificationTokenDao.delete(user);
        Assert.assertEquals(count, 1);
        Assert.assertNull(verificationTokenDao.getByUser(user));
	}
	
}
