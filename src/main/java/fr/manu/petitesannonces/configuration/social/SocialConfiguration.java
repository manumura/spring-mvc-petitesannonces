package fr.manu.petitesannonces.configuration.social;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
	
    @Autowired
    private DataSource dataSource;
    
    @Autowired
	private Environment environment;
    
    @Autowired
    private ConnectionSignUp customConnectionSignUp;
    
    private static final Logger logger = LoggerFactory.getLogger(SocialConfiguration.class);

    @Override
    public void addConnectionFactories(final ConnectionFactoryConfigurer connectionFactoryConfigurer, final Environment environment) {
    	
    	final FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
                environment.getProperty("facebook.app.id"),
                environment.getProperty("facebook.app.secret"));
    	facebookConnectionFactory.setScope(environment.getProperty("facebook.scope"));
        connectionFactoryConfigurer.addConnectionFactory(facebookConnectionFactory);
        
        connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(
            environment.getProperty("twitter.consumer.key"),
            environment.getProperty("twitter.consumer.secret")));
        
        final GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(
                environment.getProperty("google.app.id"),
                environment.getProperty("google.app.secret"));
        googleConnectionFactory.setScope(environment.getProperty("google.scope"));
        connectionFactoryConfigurer.addConnectionFactory(googleConnectionFactory);
        
//        connectionFactoryConfigurer.addConnectionFactory(new LinkedInConnectionFactory(
//            environment.getProperty("linkedin.app.id"),
//            environment.getProperty("linkedin.app.secret")));
//        
//        connectionFactoryConfigurer.addConnectionFactory(new GitHubConnectionFactory(
//            environment.getProperty("github.app.id"),
//            environment.getProperty("github.app.secret")));
//        
//        connectionFactoryConfigurer.addConnectionFactory(new LiveConnectionFactory(
//            environment.getProperty("live.app.id"),
//            environment.getProperty("live.app.secret")));
    }

	@Override
	public UserIdSource getUserIdSource() {
        // return new AuthenticationNameUserIdSource(); // Spring security
        return new CustomUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator) {
        final JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, textEncryptor());

        Boolean isImplicitSignUp = Boolean.valueOf(environment.getProperty("implicit.sign.up"));
        logger.debug(">>>>> Implicit social provider sign up : {} <<<<<", isImplicitSignUp);

        // For auto signup
        if (isImplicitSignUp) {
            repository.setConnectionSignUp(customConnectionSignUp);
        }

        return repository;
    }
    
    @Bean
	public TextEncryptor textEncryptor() {
    	// http://stackoverflow.com/questions/12619986/what-is-the-correct-way-to-configure-a-spring-textencryptor-for-use-on-heroku
		final String localEncryptionPassword = environment.getProperty("local.encryption.password");
		final String localEncryptionSalt = environment.getProperty("local.encryption.hexsalt");
		return Encryptors.delux(localEncryptionPassword, localEncryptionSalt);
//		return Encryptors.noOpText(); // Dev
	}
    
    @Bean
    public ProviderSignInUtils providerSignInUtils(final ConnectionFactoryLocator connectionFactoryLocator, final UsersConnectionRepository connectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }
    
}
