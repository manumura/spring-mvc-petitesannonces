package fr.manu.petitesannonces.configuration;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import fr.manu.petitesannonces.dbunit.H2DataTypeFactory;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
// @DataJpaTest
@ContextConfiguration(classes = { ApplicationConfigurationTest.class })
@Transactional
public abstract class TransactionalContextTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private DataSource dataSource;

//	private Server server;

	private static final Logger logger = LoggerFactory.getLogger(TransactionalContextTest.class);

//	@BeforeClass
//	public void startServer() throws SQLException {
//		server = Server.createTcpServer("-tcpPort", "9999");
//		server.start();
//		server.stop();
//	}
//
//	@AfterClass
//	public void stopServer() throws SQLException {
//		server.stop();
//	}

	@BeforeMethod
	public void setUp() throws Exception {
		logger.debug(">>>>> setUp <<<<<");
		final IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
	}

	// @AfterMethod
	// public void cleanUp() throws Exception {
	// final IDatabaseConnection connection = new
	// DatabaseDataSourceConnection(dataSource);
	// DatabaseOperation.DELETE_ALL.execute(connection, getDataSet());
	// }

	protected abstract IDataSet getDataSet() throws Exception;
}