package fr.manu.petitesannonces.business;

import java.util.List;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.dto.UserRole;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.services.UserRoleService;

/**
 * 
 * @author Manu
 *
 */
public class UserRoleBusinessTest extends TransactionalContextTest {
	
	@Autowired
	@Qualifier("userRoleService")
	private UserRoleService userRoleService;
	
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
		final List<UserRole> results = userRoleService.list();
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void testGet() throws BusinessException, TechnicalException {
		final UserRole role = userRoleService.get(1l);
		Assert.assertNotNull(role);
		Assert.assertEquals(role.getId(), Long.valueOf(1));
		Assert.assertEquals(role.getType(), "TYPE1");
		
		final UserRole role2 = userRoleService.get(3l);
		Assert.assertNull(role2);
	}
	
	@Test
	public void testGetByType() throws BusinessException, TechnicalException {
		final UserRole role = userRoleService.getByType("TYPE1");
		Assert.assertEquals(role.getId(), Long.valueOf(1));
		Assert.assertEquals(role.getType(), "TYPE1");
		
		final UserRole role2 = userRoleService.getByType("TYPE3");
		Assert.assertNull(role2);
	}
	
}
