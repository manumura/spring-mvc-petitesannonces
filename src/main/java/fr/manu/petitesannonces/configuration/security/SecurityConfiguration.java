package fr.manu.petitesannonces.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import fr.manu.petitesannonces.configuration.social.CustomSpringSocialConfigurer;
import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.persistence.services.PersistentTokenRepositoryService;
import fr.manu.petitesannonces.web.constantes.Constantes;
import fr.manu.petitesannonces.web.security.CustomAuthenticationFailureHandler;
import fr.manu.petitesannonces.web.security.CustomAuthenticationSuccessHandler;
import fr.manu.petitesannonces.web.security.CustomDaoAuthenticationProvider;
import fr.manu.petitesannonces.web.security.CustomPersistentTokenBasedRememberMeServices;
import fr.manu.petitesannonces.web.security.CustomUsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PersistentTokenRepositoryService tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    public SecurityConfiguration() {
        // Nothing to do here
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                // Spring Security ignores request to static resources such as CSS or JS files.
                .ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http

                .addFilterBefore(customUsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()

                // TODO Allow anonymous resource requests
                .antMatchers("/", RequestMappingUrl.LOGIN.getUrl(), RequestMappingUrl.LOGIN_FAILURE.getUrl(), 
                		RequestMappingUrl.LOGOUT.getUrl(), 
                		RequestMappingUrl.ACCESS_DENIED.getUrl(), RequestMappingUrl.ERROR.getUrl(), 
                		RequestMappingUrl.REGISTER.getUrl(), RequestMappingUrl.REGISTRATION_CONFIRMATION.getUrl(), 
                		RequestMappingUrl.REGISTRATION_CONFIRMATION_SUCCESS.getUrl(), RequestMappingUrl.REGISTRATION_CONFIRMATION_ERROR.getUrl(), 
                		RequestMappingUrl.RESEND_REGISTRATION_CONFIRMATION_LINK.getUrl(),
                		RequestMappingUrl.RESET_PASSWORD.getUrl(), RequestMappingUrl.CHANGE_PASSWORD.getUrl(), 
                		RequestMappingUrl.CHANGE_PASSWORD_SUCCESS.getUrl(), RequestMappingUrl.CHANGE_PASSWORD_ERROR.getUrl(), 
                		RequestMappingUrl.RESEND_PASSWORD_RESET_LINK.getUrl(), 
                        "/auth/**", RequestMappingUrl.SIGIN.getUrl(), 
                        "/resources/**", "/favicon.ico")
                .permitAll()

                .antMatchers(RequestMappingUrl.UPDATE_PASSWORD.getUrl())
                .hasAuthority(Constantes.CHANGE_PASSWORD_PRIVILEGE)

                // TODO /admin/** et /user/** + handlerInterceptor config
                // The rest of the our application is protected
                .antMatchers("/admin/**").access("hasRole('ADMIN') and isFullyAuthenticated()")
                
                .antMatchers("/user/**", "/social/connect/**",
                		RequestMappingUrl.FACEBOOK.getUrl(), RequestMappingUrl.TWITTER.getUrl(), RequestMappingUrl.GOOGLE.getUrl())
                .access("hasRole('USER') or hasRole('ADMIN') and isFullyAuthenticated()")
                
                .anyRequest().authenticated()

                .and().formLogin().loginPage(RequestMappingUrl.LOGIN.getUrl())
                .loginProcessingUrl(RequestMappingUrl.LOGIN_AUTHENTICATE.getUrl())
                .usernameParameter(Constantes.LOGIN_PARAMETER).passwordParameter(Constantes.PASSWORD_PARAMETER)
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())

                // Configures the logout function
                .and().logout().deleteCookies("JSESSIONID").logoutUrl(RequestMappingUrl.LOGOUT.getUrl())
                .logoutSuccessUrl(RequestMappingUrl.LOGOUT_SUCESSFUL.getUrl())

                .and()
                // TODO post login /signup url CustomSpringSocialConfigurer
                .apply(new CustomSpringSocialConfigurer()
                        .signupUrl(RequestMappingUrl.REGISTER.getUrl())
                        .alwaysUsePostLoginUrl(true)
                        .postLoginUrl(RequestMappingUrl.USER_EDIT.getUrl())
                        .postFailureUrl(RequestMappingUrl.LOGIN_FAILURE.getUrl()))

                .and().rememberMe().rememberMeServices(persistentTokenBasedRememberMeServices())

                .and().csrf()
                	// https://github.com/spring-projects/spring-security/issues/3906
                	.csrfTokenRepository(new HttpSessionCsrfTokenRepository())

                .and().exceptionHandling().accessDeniedPage(RequestMappingUrl.ACCESS_DENIED.getUrl());

        http.sessionManagement().maximumSessions(1)
                .expiredUrl(RequestMappingUrl.LOGIN_EXPIRED.getUrl()).and()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/")
                .sessionFixation().changeSessionId();
        
        http.authenticationProvider(authenticationProvider());
        
        logger.debug(">>>>> HTTP security : [{}] <<<<<", http);
        // servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

    // Session control support
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(socialAuthenticationProvider());
//        auth.authenticationProvider(authenticationProvider());
//        logger.debug(">>>>> AuthenticationManagerBuilder : [{}] <<<<<", auth);
//    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter()
            throws Exception {
        final CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter =
                new CustomUsernamePasswordAuthenticationFilter();
        customUsernamePasswordAuthenticationFilter
                .setAuthenticationManager(authenticationManagerBean());
        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final CustomDaoAuthenticationProvider authenticationProvider =
                new CustomDaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    // @Bean
    // public SocialAuthenticationProvider socialAuthenticationProvider() {
    // return new CustomSocialAuthenticationProvider(usersConnectionRepository,
    // socialUserDetailsService);
    // }
    
    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        final PersistentTokenBasedRememberMeServices tokenBasedservice =
                new CustomPersistentTokenBasedRememberMeServices(Constantes.REMEMBER_ME_KEY,
                        userDetailsService, tokenRepository);
        tokenBasedservice.setTokenValiditySeconds(86400);
        tokenBasedservice.setAlwaysRemember(true);
        return tokenBasedservice;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

}
