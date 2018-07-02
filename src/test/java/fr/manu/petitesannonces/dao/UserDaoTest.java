package fr.manu.petitesannonces.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.dao.UserDao;
import fr.manu.petitesannonces.persistence.entities.UserEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * 
 * @author Manu
 *
 */
public class UserDaoTest extends TransactionalContextTest {
	
	@Autowired
    @Qualifier("userDao")
    private UserDao userDao;
	
    @Autowired
    private Converter converter;

    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

	/*
	 * @see fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
        final IDataSet[] datasets = new IDataSet[] {
                new FlatXmlDataSet(
                        this.getClass().getClassLoader().getResourceAsStream("user_account.xml")),
                new FlatXmlDataSet(
                        this.getClass().getClassLoader().getResourceAsStream("user_role.xml")),
                new FlatXmlDataSet(this.getClass().getClassLoader()
                        .getResourceAsStream("user_account_user_role.xml"))};
        IDataSet dataSet = new CompositeDataSet(datasets);
        logger.debug("Datasets : {}", dataSet);
        return dataSet;
	}
	
	@Test
	public void testList() throws BusinessException, TechnicalException {
		logger.debug(">>>>> testList : start <<<<<");
        final List<UserEntity> results = userDao.list();
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		Assert.assertEquals(2, results.size());
		logger.debug(">>>>> testList : end <<<<<");
	}

	@Test
	public void testGet() throws BusinessException, TechnicalException {
		logger.debug(">>>>> testGet : start <<<<<");
        final UserEntity user = userDao.get(1l);
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
		logger.debug(">>>>> testGet : end <<<<<");
	}
	
    // @Test(expectedExceptions = NotFoundException.class)
	public void testGetWithException() throws BusinessException, TechnicalException {
		logger.debug(">>>>> testGetWithException : start <<<<<");
        final UserEntity user = userDao.get(3l);
        Assert.assertNull(user);
        logger.debug(">>>>> testGetWithException : end <<<<<");
	}
	
	@Test
	public void testGetByLogin() throws BusinessException, TechnicalException {
		logger.debug(">>>>> testGetByLogin : start <<<<<");
        final UserEntity user = userDao.getByLogin("manu");
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
		logger.debug(">>>>> testGetByLogin : end <<<<<");
	}
	
//     @Test(expectedExceptions = NoResultException.class)
    @Test
	public void testGetByLoginWithException() throws BusinessException, TechnicalException {
    	logger.debug(">>>>> testGetByLoginWithException : start <<<<<");
        UserEntity user = userDao.getByLogin("test");
        Assert.assertNull(user);
        logger.debug(">>>>> testGetByLoginWithException : end <<<<<");
	}
	
	@Test
	public void testCreate() throws BusinessException, TechnicalException {

		logger.debug(">>>>> testCreate : start <<<<<");
        final UserEntity user = userDao.create(getSampleUser(null));
		
        // final LocalDateTime now = new LocalDateTime();
        // final LocalDateTime dateMin = now.minusSeconds(10);
        // final LocalDateTime dateMax = now.plusSeconds(10);
		
		Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        // Assert.assertNotNull(user.getId(), Long.valueOf(3));
		Assert.assertEquals(user.getLogin(), "johnny");
//		Assert.assertEquals(user.getPassword(), "pass4johnny");
		Assert.assertEquals(user.getEmail(), "johnny@xyz.com");
		Assert.assertEquals(user.getNom(), "John");
		Assert.assertEquals(user.getPrenom(), "Doe");
		Assert.assertEquals(user.getAdressePrincipal(), "5, rue johnny");
		Assert.assertEquals(user.getAdresseDetail(), "complement johnny");
		Assert.assertEquals(user.getCodePostal(), Integer.valueOf(12345));
		Assert.assertEquals(user.getVille(), "ville");
		Assert.assertEquals(user.getPays(), "FRANCE");
        // Assert.assertTrue(user.getDateInscription().isAfter(dateMin));
        // Assert.assertTrue(user.getDateInscription().isBefore(dateMax));
        Assert.assertEquals(user.getDateInscription(), new LocalDateTime(2016, 3, 12, 18, 39, 59));
		Assert.assertEquals(user.getDateNaissance(), new LocalDate(1999, 1, 20));
		Assert.assertEquals(user.getDeleted(), Boolean.FALSE);
        Assert.assertEquals(user.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(user.getLastAction(), new LocalDateTime(2016, 6, 5, 23, 32, 29));

        // TODO
        // Remove user after test
        userDao.remove(user.getId());
        logger.debug(">>>>> testCreate : end <<<<<");
	}
	
	@Test
	public void testUpdate() throws BusinessException, TechnicalException {
		logger.debug(">>>>> testUpdate : start <<<<<");
        final UserEntity user = userDao.get(1l);

        // update manu with johnny
        final UserEntity userToUpdate = converter.convert(getSampleUser(null), user);
        userToUpdate.setId(1l);
		
        final UserEntity userUpdated = userDao.update(userToUpdate);
		Assert.assertNotNull(userUpdated);
		Assert.assertEquals(userUpdated.getId(), Long.valueOf(1));
		Assert.assertEquals(userUpdated.getLogin(), "johnny");
//		Assert.assertEquals(userUpdated.getPassword(), "pass4johnny");
		Assert.assertEquals(userUpdated.getEmail(), "johnny@xyz.com");
		Assert.assertEquals(userUpdated.getNom(), "John");
		Assert.assertEquals(userUpdated.getPrenom(), "Doe");
		Assert.assertEquals(userUpdated.getAdressePrincipal(), "5, rue johnny");
		Assert.assertEquals(userUpdated.getAdresseDetail(), "complement johnny");
		Assert.assertEquals(userUpdated.getCodePostal(), Integer.valueOf(12345));
		Assert.assertEquals(userUpdated.getVille(), "ville");
		Assert.assertEquals(userUpdated.getPays(), "FRANCE");
		Assert.assertEquals(userUpdated.getDateInscription(), new LocalDateTime(2016, 3, 12, 18, 39, 59));
		Assert.assertEquals(userUpdated.getDateNaissance(), new LocalDate(1999, 1, 20));
		Assert.assertEquals(userUpdated.getDeleted(), Boolean.FALSE);
		Assert.assertEquals(userUpdated.getEmailConfirmed(), Boolean.TRUE);
		Assert.assertEquals(userUpdated.getLastAction(), new LocalDateTime(2016, 6, 5, 23, 32, 29));
		
        // UserEntity user3 = userDao.get(3l);
        // Assert.assertNull(user3);
		logger.debug(">>>>> testUpdate : end <<<<<");
	}
	
    private UserEntity getSampleUser(final Long id) {
        final UserEntity user = new UserEntity();
		user.setId(id);
		user.setLogin("johnny");
		user.setPassword("pass4johnny");
		user.setEmail("johnny@xyz.com");
		user.setNom("John");
		user.setPrenom("Doe");
		user.setAdressePrincipal("5, rue johnny");
		user.setAdresseDetail("complement johnny");
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
