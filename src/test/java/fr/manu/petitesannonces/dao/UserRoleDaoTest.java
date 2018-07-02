package fr.manu.petitesannonces.dao;

import java.util.List;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.persistence.dao.UserRoleDao;
import fr.manu.petitesannonces.persistence.entities.UserRoleEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * 
 * @author Manu
 *
 */
public class UserRoleDaoTest extends TransactionalContextTest {
	
	@Autowired
    @Qualifier("userRoleDao")
    private UserRoleDao userRoleDao;
	
	/*
	 * @see fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		final IDataSet[] datasets = new IDataSet[] {
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account.xml")),
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_role.xml")),
				new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("user_account_user_role.xml"))
		};
		return new CompositeDataSet(datasets);
	}
	
	@Test
	public void testList() throws BusinessException, TechnicalException {
        final List<UserRoleEntity> results = userRoleDao.list();
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void testGet() throws BusinessException, TechnicalException {
        final UserRoleEntity role = userRoleDao.get(1l);
		Assert.assertNotNull(role);
		Assert.assertEquals(role.getId(), Long.valueOf(1));
		Assert.assertEquals(role.getType(), "TYPE1");
		
        final UserRoleEntity role2 = userRoleDao.get(3l);
		Assert.assertNull(role2);
	}
	
	@Test
	public void testGetByType() throws BusinessException, TechnicalException {
        final UserRoleEntity role = userRoleDao.getByType("TYPE1");
		Assert.assertEquals(role.getId(), Long.valueOf(1));
		Assert.assertEquals(role.getType(), "TYPE1");
		
        final UserRoleEntity role2 = userRoleDao.getByType("TYPE3");
		Assert.assertNull(role2);
	}
	
}
