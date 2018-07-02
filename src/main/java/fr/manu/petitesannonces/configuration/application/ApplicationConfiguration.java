package fr.manu.petitesannonces.configuration.application;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
//import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.web.interceptor.TokenValidatorHandlerInterceptor;
//import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "fr.manu.petitesannonces")
@PropertySource({ "classpath:social.properties", "classpath:recaptcha.properties" })
public class ApplicationConfiguration 
//	extends WebMvcConfigurationSupport {
     implements WebMvcConfigurer {

    private ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework
     * .context.ApplicationContext)
     */
//    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
//		messageSource.setAlwaysUseMessageFormat(true);
		return messageSource;
	}
	
	@Bean(name = "reloadableResourceBundleMessageSource")
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
//      messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
	
	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver() {
		final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}
	
	@Override
    public void addInterceptors(final InterceptorRegistry registry) {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("locale");
		registry.addInterceptor(interceptor);
    }
	
	@Bean(name = "validator")
	public LocalValidatorFactoryBean validator() {
	    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
	    validatorFactoryBean.setValidationMessageSource(messageSource());
	    return validatorFactoryBean;
	}

	@Override
	public Validator getValidator() {
	    return validator();
	}

	@Bean(name = "converter")
	public Converter converter() {
		return new Converter();
	}
	
	@Bean(name = "handlerInterceptor")
    public MappedInterceptor getMappedInterceptor(final TokenValidatorHandlerInterceptor customHandlerInterceptor) {
        return new MappedInterceptor(
        		//TODO intercepted URLs
        		new String[] { "/user/**", "/admin/**" }, 
        		new String[] { "/", "/login", "/logout", "/access-denied", "/error", "/register", "/resources/**" }, 
        		customHandlerInterceptor);
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**", "/static/**").addResourceLocations("/resources/", "/static/", "classpath:/static/")
			.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
	}
	
	/**
	 * Ensures that placeholders are replaced with property values
	 * @return PropertySourcesPlaceholderConfigurer
	 */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    /**
     * Thymeleaf template resolver
     * @return ITemplateResolver
     */
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
	    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
	    resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setApplicationContext(applicationContext);
	    resolver.setCacheable(false);
        // Default is no TTL (only LRU would remove entries)
        // resolver.setCacheTTLMs(60000L);
        // resolver.getCacheablePatternSpec().addPattern("/**");
	    return resolver;
	}
    
    /**
     * Thymeleaf template engine
     * @return TemplateEngine
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
    	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // templateEngine.setEnableSpringELCompiler(true);
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());
        // templateEngine.addDialect(new SpringSocialDialect());
		templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }

	@Bean(name = "viewResolver")
    public ViewResolver viewResolver() {
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

}
