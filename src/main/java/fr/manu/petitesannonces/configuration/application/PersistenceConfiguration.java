package fr.manu.petitesannonces.configuration.application;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "fr.manu.petitesannonces.persistence"
})
@PropertySource({ "classpath:jdbc.properties", "classpath:hibernate.properties" })
public class PersistenceConfiguration {

	@Autowired
	private Environment environment;

	private static final String HIBERNATE_CONNECTION_AUTOCOMMIT = "hibernate.connection.autocommit";

	private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

	private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

	private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

//	private static final String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "hibernate.current_session_context_class";

	private static final String HIBERNATE_DIALECT = "hibernate.dialect";

	private static final String HIBERNATE_CLASSLOADING_USE_CURRENT_TCCL_AS_PARENT = "hibernate.classloading.use_current_tccl_as_parent";
	
	private static final String HIBERNATE_EJB_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";

	private static final String PACKAGE_TO_SCAN = "fr.manu.petitesannonces.persistence";

	@Bean
	public LocalContainerEntityManagerFactoryBean  entityManagerFactory(final DataSource datasouce) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(PACKAGE_TO_SCAN);
        entityManagerFactoryBean.setJpaProperties(entityManagerProperties());
        return entityManagerFactoryBean;
	}
	
	@Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

//	@Bean
//	public LocalSessionFactoryBean sessionFactory(final DataSource datasouce) {
//		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(datasouce);
//		sessionFactory.setPackagesToScan(PACKAGE_TO_SCAN);
//		sessionFactory.setHibernateProperties(hibernateProperties());
//		return sessionFactory;
//	}
	
//	@Bean
//	public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
//		final HibernateTransactionManager txManager = new HibernateTransactionManager(sessionFactory);
//		return txManager;
//	}

//	@Bean
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
//		 final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		 dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
//		 dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
//		 dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
//		 dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
//		 return dataSource;

		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSourceConfig.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		dataSourceConfig.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSourceConfig.setPassword(environment.getRequiredProperty("jdbc.password"));
		dataSourceConfig.setMaximumPoolSize(3);
		
		// MySQL
		dataSourceConfig.setAutoCommit(true);
		dataSourceConfig.addDataSourceProperty("cachePrepStmts", "true");
		dataSourceConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		dataSourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		dataSourceConfig.addDataSourceProperty("useServerPrepStmts", "true");
	    
		return new HikariDataSource(dataSourceConfig);
	}

	private Properties entityManagerProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.put(HIBERNATE_DIALECT, environment.getRequiredProperty(HIBERNATE_DIALECT));
//		hibernateProperties.put(HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS,
//				environment.getRequiredProperty(HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS));
		hibernateProperties.put(HIBERNATE_CONNECTION_AUTOCOMMIT,
				environment.getRequiredProperty(HIBERNATE_CONNECTION_AUTOCOMMIT));
		hibernateProperties.put(HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(HIBERNATE_FORMAT_SQL));
		hibernateProperties.put(HIBERNATE_HBM2DDL_AUTO, environment.getRequiredProperty(HIBERNATE_HBM2DDL_AUTO));
		hibernateProperties.put(HIBERNATE_SHOW_SQL, environment.getRequiredProperty(HIBERNATE_SHOW_SQL));
		hibernateProperties.put(HIBERNATE_CLASSLOADING_USE_CURRENT_TCCL_AS_PARENT,
				environment.getRequiredProperty(HIBERNATE_CLASSLOADING_USE_CURRENT_TCCL_AS_PARENT));
		//Configures the naming strategy that is used when Hibernate creates
        //new database objects and schema elements
		hibernateProperties.put(HIBERNATE_EJB_NAMING_STRATEGY, environment.getRequiredProperty(HIBERNATE_EJB_NAMING_STRATEGY));
		// hibernateProperties.put(hibernate_connection_provider_class,
		// env.getProperty(hibernate_connection_provider_class));
		return hibernateProperties;
	}
	
}