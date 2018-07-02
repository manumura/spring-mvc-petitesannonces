package fr.manu.petitesannonces.business;

import java.util.List;

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
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserService;

/**
 * 
 * @author Manu
 *
 */
public class UserBusinessTest extends TransactionalContextTest {
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	/*
	 * @see fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		final IDataSet dataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account.xml"));
		return dataSet;
	}
	
	@Test
	public void testList() throws BusinessException, TechnicalException {
		final List<User> results = userService.list();
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void testGet() throws BusinessException, TechnicalException {
		final User user = userService.get(1l);
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), Long.valueOf(1));
		Assert.assertEquals(user.getLogin(), "manu");
		Assert.assertEquals(user.getPassword(), "manolo");
		Assert.assertEquals(user.getEmail(), "manu@xyz.com");
		Assert.assertEquals(user.getNom(), "Mura");
		Assert.assertEquals(user.getPrenom(), "Manu");
		Assert.assertEquals(user.getAdressePrincipal(), "1, rue du cul");
		Assert.assertEquals(user.getAdresseDetail(), "porte de derriere");
		Assert.assertEquals(user.getCodePostal(), Integer.valueOf(33000));
		Assert.assertEquals(user.getVille(), "Bordeaux");
		Assert.assertEquals(user.getPays(), "France");
		Assert.assertEquals(user.getDateInscription(), new LocalDateTime(2016, 6, 18, 0, 0, 0));
		Assert.assertEquals(user.getDateNaissance(), new LocalDate(1981, 8, 14));
		Assert.assertEquals(user.getDeleted(), Boolean.FALSE);
		Assert.assertEquals(user.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(user.getLastAction(), new LocalDateTime(2016, 8, 8, 14, 36, 45));
	}
	
    // @Test(expectedExceptions = NotFoundException.class)
    @Test
	public void testGetWithException() throws BusinessException, TechnicalException {
        User user = userService.get(3l);
        Assert.assertNull(user);
	}
	
	@Test
	public void testGetByLogin() throws BusinessException, TechnicalException {
		final User user = userService.getByLogin("manu");
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), Long.valueOf(1));
		Assert.assertEquals(user.getLogin(), "manu");
		Assert.assertEquals(user.getPassword(), "manolo");
		Assert.assertEquals(user.getEmail(), "manu@xyz.com");
		Assert.assertEquals(user.getNom(), "Mura");
		Assert.assertEquals(user.getPrenom(), "Manu");
		Assert.assertEquals(user.getAdressePrincipal(), "1, rue du cul");
		Assert.assertEquals(user.getAdresseDetail(), "porte de derriere");
		Assert.assertEquals(user.getCodePostal(), Integer.valueOf(33000));
		Assert.assertEquals(user.getVille(), "Bordeaux");
		Assert.assertEquals(user.getPays(), "France");
		Assert.assertEquals(user.getDateInscription(), new LocalDateTime(2016, 6, 18, 0, 0, 0));
		Assert.assertEquals(user.getDateNaissance(), new LocalDate(1981, 8, 14));
		Assert.assertEquals(user.getDeleted(), Boolean.FALSE);
		Assert.assertEquals(user.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(user.getLastAction(), new LocalDateTime(2016, 8, 8, 14, 36, 45));
	}
	
    // @Test(expectedExceptions = NotFoundException.class)
    @Test
	public void testGetByLoginWithException() throws BusinessException, TechnicalException {
        User user = userService.getByLogin("test");
        Assert.assertNull(user);
	}
	
	@Test
	public void testIsUserLoginUnique() throws BusinessException, TechnicalException {
		final boolean result = userService.isUserLoginUnique(1l, "manu");
		Assert.assertTrue(result);
		
		final boolean result2 = userService.isUserLoginUnique(3l, "test");
		Assert.assertTrue(result2);
		
		final boolean result3 = userService.isUserLoginUnique(3l, "manu");
		Assert.assertFalse(result3);
	}
	
	@Test
	public void testCreate() throws BusinessException, TechnicalException {
		
        final User user = userService.create(getSampleUser(null));
		
        // final User user = userService.get(3l);
		
		final LocalDateTime now = new LocalDateTime();
		final LocalDateTime dateMin = now.minusSeconds(10);
		final LocalDateTime dateMax = now.plusSeconds(10);
		
		Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        // Assert.assertEquals(user.getId(), Long.valueOf(3));
        Assert.assertEquals(user.getLogin(), "albert");
//		Assert.assertEquals(user.getPassword(), "pass4johnny");
        Assert.assertEquals(user.getEmail(), "albert@xyz.com");
        Assert.assertEquals(user.getNom(), "ALBERT");
        Assert.assertEquals(user.getPrenom(), "Albert");
        Assert.assertEquals(user.getAdressePrincipal(), "5, rue albert");
        Assert.assertEquals(user.getAdresseDetail(), "complement albert");
		Assert.assertEquals(user.getCodePostal(), Integer.valueOf(12345));
		Assert.assertEquals(user.getVille(), "ville");
		Assert.assertEquals(user.getPays(), "FRANCE");
		Assert.assertTrue(user.getDateInscription().isAfter(dateMin));
		Assert.assertTrue(user.getDateInscription().isBefore(dateMax));
		Assert.assertEquals(user.getDateNaissance(), new LocalDate(1999, 1, 20));
		Assert.assertEquals(user.getDeleted(), Boolean.FALSE);
        Assert.assertEquals(user.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(user.getLastAction(), new LocalDateTime(2016, 6, 5, 23, 32, 29));

        // TODO
        // Remove user after test
        userService.delete(user.getId());
	}
	
	@Test
	public void testUpdate() throws BusinessException, TechnicalException {
		final User user = getSampleUser(1l); // update manu with johnny
		
		final User userUpdated = userService.update(user);
		Assert.assertNotNull(userUpdated);
		Assert.assertEquals(userUpdated.getId(), Long.valueOf(1));
        Assert.assertEquals(userUpdated.getLogin(), "albert");
//		Assert.assertEquals(userUpdated.getPassword(), "pass4johnny");
        Assert.assertEquals(userUpdated.getEmail(), "albert@xyz.com");
        Assert.assertEquals(userUpdated.getNom(), "ALBERT");
        Assert.assertEquals(userUpdated.getPrenom(), "Albert");
        Assert.assertEquals(userUpdated.getAdressePrincipal(), "5, rue albert");
        Assert.assertEquals(userUpdated.getAdresseDetail(), "complement albert");
		Assert.assertEquals(userUpdated.getCodePostal(), Integer.valueOf(12345));
		Assert.assertEquals(userUpdated.getVille(), "ville");
		Assert.assertEquals(userUpdated.getPays(), "FRANCE");
		Assert.assertEquals(userUpdated.getDateInscription(), new LocalDateTime(2016, 3, 12, 18, 39, 59));
		Assert.assertEquals(userUpdated.getDateNaissance(), new LocalDate(1999, 1, 20));
		Assert.assertEquals(userUpdated.getDeleted(), Boolean.FALSE);
		Assert.assertEquals(userUpdated.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(userUpdated.getLastAction(), new LocalDateTime(2016, 6, 5, 23, 32, 29));
		
        User otherUser = userService.get(3l);
        Assert.assertNull(otherUser);
	}
	
	@Test
    public void testMarkAsDeleted() throws BusinessException, TechnicalException {
		
		final User userRemoved = userService.markAsDeleted(1l);
		Assert.assertNotNull(userRemoved);
		Assert.assertEquals(userRemoved.getId(), Long.valueOf(1));
		Assert.assertEquals(userRemoved.getLogin(), "manu");
		Assert.assertEquals(userRemoved.getPassword(), "manolo");
		Assert.assertEquals(userRemoved.getEmail(), "manu@xyz.com");
		Assert.assertEquals(userRemoved.getNom(), "Mura");
		Assert.assertEquals(userRemoved.getPrenom(), "Manu");
		Assert.assertEquals(userRemoved.getAdressePrincipal(), "1, rue du cul");
		Assert.assertEquals(userRemoved.getAdresseDetail(), "porte de derriere");
		Assert.assertEquals(userRemoved.getCodePostal(), Integer.valueOf(33000));
		Assert.assertEquals(userRemoved.getVille(), "Bordeaux");
		Assert.assertEquals(userRemoved.getPays(), "France");
		Assert.assertEquals(userRemoved.getDateInscription(), new LocalDateTime(2016, 6, 18, 0, 0, 0));
		Assert.assertEquals(userRemoved.getDateNaissance(), new LocalDate(1981, 8, 14));
		Assert.assertEquals(userRemoved.getDeleted(), Boolean.TRUE);
		Assert.assertEquals(userRemoved.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(userRemoved.getLastAction(), new LocalDateTime(2016, 8, 8, 14, 36, 45));
	}
	
	@Test
    public void testDelete() throws BusinessException, TechnicalException {
        userService.delete(1l);
		
        User user = userService.get(1l);
        Assert.assertNull(user);
	}
	
	private User getSampleUser(final Long id) {
		final User user = new User();
		user.setId(id);
        user.setLogin("albert");
        user.setPassword("albertpass");
        user.setEmail("albert@xyz.com");
        user.setNom("ALBERT");
        user.setPrenom("Albert");
        user.setAdressePrincipal("5, rue albert");
        user.setAdresseDetail("complement albert");
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
