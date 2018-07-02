package fr.manu.petitesannonces.business;

import java.util.Calendar;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.dto.VerificationToken;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.persistence.services.VerificationTokenService;

/**
 * 
 * @author Manu
 *
 */
public class VerificationTokenBusinessTest extends TransactionalContextTest {
	
	@Autowired
	@Qualifier("verificationTokenService")
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
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
		final VerificationToken result = verificationTokenService.getByToken("7a0ba0b6-9b30-4493-997a-6ae1a5f6ad85");
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

        final User user = userService.create(getSampleUser());
        // final User user = userService.get(1l);
		
		final VerificationToken resultCreate = verificationTokenService.create(user, "111-222-333");
		Assert.assertNotNull(resultCreate);
		Assert.assertEquals(3l, resultCreate.getId().longValue());
		Assert.assertEquals("111-222-333", resultCreate.getToken());
//		Assert.assertEquals(3l, resultCreate.getUser().getId().longValue());
        Assert.assertEquals("jacqueline", resultCreate.getUser().getLogin());
		Assert.assertNotNull(resultCreate.getExpiryDate());
		
		final VerificationToken resultGet = verificationTokenService.getByUser(user);
		Assert.assertNotNull(resultGet);
		Assert.assertEquals(3l, resultGet.getId().longValue());
		Assert.assertEquals("111-222-333", resultGet.getToken());
//		Assert.assertEquals(3l, resultGet.getUser().getId().longValue());
        Assert.assertEquals("jacqueline", resultGet.getUser().getLogin());
		Assert.assertNotNull(resultGet.getExpiryDate());
		
        // TODO
        // final int count = verificationTokenService.delete(user);
        // Assert.assertEquals(count, 1);
        // Assert.assertNull(verificationTokenService.getByUser(user));

        // TODO
        // Remove user after test
        userService.delete(user.getId());
	}
	
	private User getSampleUser() {
		final User user = new User();
        user.setLogin("jacqueline");
        user.setPassword("jacquelinepass");
        user.setEmail("jacqueline@xyz.com");
        user.setNom("JACQUELINE");
        user.setPrenom("Jacqueline");
        user.setAdressePrincipal("5, rue jacqueline");
        user.setAdresseDetail("complement jacqueline");
		user.setCodePostal(12345);
		user.setVille("ville");
		user.setPays("FRANCE");
		user.setDateInscription(new LocalDateTime(2016, 3, 12, 18, 39, 59));
		user.setDateNaissance(new LocalDate(1999, 1, 20));
		user.setDeleted(false);
		user.setEmailConfirmed(true);
		user.setLastAction(new LocalDateTime(2016, 6, 5, 23, 32, 29));
		return user;
	}
	
}
