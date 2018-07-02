package fr.manu.petitesannonces.configuration.application;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This is servlet initializer class replace for web.xml file.
 */
public class ServletContainerInitializer implements WebApplicationInitializer {
	
    @Override
    public void onStartup(ServletContext container) {
    	
        AnnotationConfigWebApplicationContext context
          = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("fr.manu.petitesannonces");
 
        container.addListener(new ContextLoaderListener(context));
 
        ServletRegistration.Dynamic dispatcher = container
          .addServlet("dispatcher", new DispatcherServlet(context));
         
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
    
}

//public class ServletContainerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		
////		servletContext.addFilter("openSessionInViewFilter", openSessionInViewFilter()).addMappingForUrlPatterns(null,
////				false, "/*");
//        servletContext.addFilter("openEntityManagerInViewFilter", openEntityManagerInViewFilter())
//                .addMappingForUrlPatterns(null, false, "/*");
//		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter()).addMappingForUrlPatterns(null,
//				true, "/*");
//		servletContext.addFilter("hiddenHttpMethodFilter", hiddenHttpMethodFilter()).addMappingForUrlPatterns(null,
//				true, "/*");
//		super.onStartup(servletContext);
//	}
//
//	/**
//	 * Common configuration.
//	 */
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//        return new Class<?>[] {PersistenceConfiguration.class, SecurityConfiguration.class,
//                SocialConfiguration.class, RestTemplateConfiguration.class,
//                ApplicationConfiguration.class};
//	}
//
//	/**
//	 * SpringMVC configuration.
//	 */
////	@Override
////	protected Class<?>[] getServletConfigClasses() {
////		return new Class[] { ApplicationContext.class };
////	}
//	
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class<?>[] {};
//	}
//
//	@Override
//	protected String[] getServletMappings() {
//		return new String[] { "/" };
//	}
//
//	@Override
//	protected String getServletName() {
//		return Constantes.APPLICATION_NAME;
//	}
//
//	@Override
//	protected boolean isAsyncSupported() {
//		return true;
//	}
//
//    // private Filter openSessionInViewFilter() {
//    // return new OpenSessionInViewFilter();
//    // }
//
//    private Filter openEntityManagerInViewFilter() {
//        return new OpenEntityManagerInViewFilter();
//    }
//
//	private Filter characterEncodingFilter() {
//		final CharacterEncodingFilter cef = new CharacterEncodingFilter();
//		cef.setEncoding("UTF-8");
//		return cef;
//	}
//
//	private Filter hiddenHttpMethodFilter() {
//		return new HiddenHttpMethodFilter();
//	}
//
//}
