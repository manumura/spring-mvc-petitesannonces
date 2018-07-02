package fr.manu.petitesannonces.business;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.configuration.TransactionalContextTest;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;

/**
 * 
 * @author Manu
 *
 */
public class UserDetailsBusinessTest extends TransactionalContextTest {

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;

	/*
	 * @see
	 * fr.manu.petitesannonces.configuration.TestTransactionalContext#getDataSet
	 * ()
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
	public void testLoadUserByUsername() throws BusinessException, TechnicalException {
		final UserDetails userDetails = userDetailsService.loadUserByUsername("manu");
		Assert.assertNotNull(userDetails);
		Assert.assertEquals(userDetails.getUsername(), "manu");
		Assert.assertEquals(userDetails.getPassword(), "manolo");
		Assert.assertTrue(!userDetails.getAuthorities().isEmpty());
		Assert.assertNotNull(userDetails.getAuthorities().iterator().next());
		Assert.assertEquals(userDetails.getAuthorities().iterator().next().getAuthority(), "ROLE_TYPE1");
	}

}
