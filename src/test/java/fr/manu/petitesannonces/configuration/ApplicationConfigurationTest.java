package fr.manu.petitesannonces.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import fr.manu.petitesannonces.web.properties.RecaptchaProperties;

/**
 * This class is same as real PersistenceConfiguration class in sources.
 * Only difference is that method dataSource and hibernateProperties 
 * implementations are specific to Hibernate working with H2 database.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "fr.manu.petitesannonces.persistence" })
@PropertySource({ "classpath:jdbc.properties", "classpath:hibernate.properties" })
public class ApplicationConfigurationTest {

	@Autowired
	private Environment environment;
	
	@Bean
	public LocalContainerEntityManagerFactoryBean  entityManagerFactory(final DataSource datasouce) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(new String[] { "fr.manu.petitesannonces" });
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
//	public LocalSessionFactoryBean sessionFactory() {
//		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource());
//		sessionFactory.setPackagesToScan(new String[] { "fr.manu.petitesannonces" });
//		sessionFactory.setHibernateProperties(entityManagerProperties());
//		return sessionFactory;
//	}
	
//	@Bean
//	public HibernateTransactionManager transactionManager(SessionFactory s) {
//		HibernateTransactionManager txManager = new HibernateTransactionManager();
//		txManager.setSessionFactory(s);
//		return txManager;
//	}

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		return dataSource;
		
//		HikariConfig dataSourceConfig = new HikariConfig();
//		dataSourceConfig.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
//		dataSourceConfig.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
//		dataSourceConfig.setUsername(environment.getRequiredProperty("jdbc.username"));
//		dataSourceConfig.setPassword(environment.getRequiredProperty("jdbc.password"));
//		dataSourceConfig.setMaximumPoolSize(3);
//		dataSourceConfig.setAutoCommit(false);
//		return new HikariDataSource(dataSourceConfig);
	}

	private Properties entityManagerProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.classloading.use_current_tccl_as_parent", 
				environment.getProperty("hibernate.classloading.use_current_tccl_as_parent"));
		return properties;
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public RecaptchaProperties getRecaptchaProperties(@Value("${recaptcha.url}") String recaptchaUrl,
            @Value("${recaptcha.site.key}") String recaptchaSecretKey,
            @Value("${recaptcha.secret.key}") String recaptchaSiteKey) {
		return new RecaptchaProperties(recaptchaUrl, recaptchaSecretKey, recaptchaSiteKey);
	}
	
}
