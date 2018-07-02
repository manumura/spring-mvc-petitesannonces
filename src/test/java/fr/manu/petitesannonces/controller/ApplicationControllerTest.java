package fr.manu.petitesannonces.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fr.manu.petitesannonces.dto.User;
import fr.manu.petitesannonces.persistence.services.UserRoleService;
import fr.manu.petitesannonces.persistence.services.UserService;
import fr.manu.petitesannonces.web.controller.ApplicationController;
import fr.manu.petitesannonces.web.properties.RecaptchaProperties;
import fr.manu.petitesannonces.web.properties.SocialFacebookProperties;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {ApplicationContext.class, ApplicationConfiguration.class})
//@WebAppConfiguration
public class ApplicationControllerTest {
	
	private MockMvc mockMvc;
	 
//    @Autowired
//    private WebApplicationContext webApplicationContext;
    
    @InjectMocks
	private ApplicationController applicationController;

    @Mock
	private UserService userService;
	
    @Mock
	private UserRoleService userRoleService;
    
    @Mock
	private RecaptchaProperties recaptchaProperties;

    @Mock
    private SocialFacebookProperties socialFacebookProperties;
	
//    @Mock
//	private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
//	@Mock
//	private AuthenticationTrustResolver authenticationTrustResolver;
//
//	@Mock
//	private MessageSource messageSource;
//
//	@Mock
//	private UserValidator validator;
//	
	
	
	@Spy
	private List<User> users = new ArrayList<User>();

//	@Mock
//	private Model model;
//	
//	@Mock
//	private BindingResult result;
//	
//	@Mock
//	private	Principal principal;
//	
//	@Mock
//	private Authentication authentication;
//	
//	@Mock
//	private SecurityContext securityContext;
	
	@BeforeClass
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		users = this.getUsersList();
		
		when(recaptchaProperties.getRecaptchaSiteKey()).thenReturn("site.key");
        when(socialFacebookProperties.getFacebookScope()).thenReturn("facebook.scope");
		
//		when(authentication.getPrincipal()).thenReturn(principal);
//		when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
	}
	
	@Test
	public void list() throws Exception {
		
		when(userService.list()).thenReturn(users);
		
		mockMvc.perform(get("/admin/list"))
	        .andExpect(status().isOk())
	        .andExpect(model().attribute("users", users));
		
//		when(userService.list()).thenReturn(users);
//		Assert.assertEquals(applicationController.list(model), "user/user-list");
//		Assert.assertEquals(model.asMap().get("users"), users);
//		verify(userService, atLeastOnce()).list();
	}
	
//	@Test
//	public void newEmployee(){
//		Assert.assertEquals(appController.newEmployee(model), "registration");
//		Assert.assertNotNull(model.get("employee"));
//		Assert.assertFalse((Boolean)model.get("edit"));
//		Assert.assertEquals(((Employee)model.get("employee")).getId(), 0);
//	}
//
//
//	@Test
//	public void saveEmployeeWithValidationError(){
//		when(result.hasErrors()).thenReturn(true);
//		doNothing().when(service).saveEmployee(any(Employee.class));
//		Assert.assertEquals(appController.saveEmployee(employees.get(0), result, model), "registration");
//	}
//
//	@Test
//	public void saveEmployeeWithValidationErrorNonUniqueSSN(){
//		when(result.hasErrors()).thenReturn(false);
//		when(service.isEmployeeSsnUnique(anyInt(), anyString())).thenReturn(false);
//		Assert.assertEquals(appController.saveEmployee(employees.get(0), result, model), "registration");
//	}
//
//	
//	@Test
//	public void saveEmployeeWithSuccess(){
//		when(result.hasErrors()).thenReturn(false);
//		when(service.isEmployeeSsnUnique(anyInt(), anyString())).thenReturn(true);
//		doNothing().when(service).saveEmployee(any(Employee.class));
//		Assert.assertEquals(appController.saveEmployee(employees.get(0), result, model), "success");
//		Assert.assertEquals(model.get("success"), "Employee Axel registered successfully");
//	}
//
//	@Test
//	public void editEmployee(){
//		Employee emp = employees.get(0);
//		when(service.findEmployeeBySsn(anyString())).thenReturn(emp);
//		Assert.assertEquals(appController.editEmployee(anyString(), model), "registration");
//		Assert.assertNotNull(model.get("employee"));
//		Assert.assertTrue((Boolean)model.get("edit"));
//		Assert.assertEquals(((Employee)model.get("employee")).getId(), 1);
//	}
//
//	@Test
//	public void updateEmployeeWithValidationError(){
//		when(result.hasErrors()).thenReturn(true);
//		doNothing().when(service).updateEmployee(any(Employee.class));
//		Assert.assertEquals(appController.updateEmployee(employees.get(0), result, model,""), "registration");
//	}
//
//	@Test
//	public void updateEmployeeWithValidationErrorNonUniqueSSN(){
//		when(result.hasErrors()).thenReturn(false);
//		when(service.isEmployeeSsnUnique(anyInt(), anyString())).thenReturn(false);
//		Assert.assertEquals(appController.updateEmployee(employees.get(0), result, model,""), "registration");
//	}
//
//	@Test
//	public void updateEmployeeWithSuccess(){
//		when(result.hasErrors()).thenReturn(false);
//		when(service.isEmployeeSsnUnique(anyInt(), anyString())).thenReturn(true);
//		doNothing().when(service).updateEmployee(any(Employee.class));
//		Assert.assertEquals(appController.updateEmployee(employees.get(0), result, model, ""), "success");
//		Assert.assertEquals(model.get("success"), "Employee Axel updated successfully");
//	}
//	
//	
//	@Test
//	public void deleteEmployee(){
//		doNothing().when(service).deleteEmployeeBySsn(anyString());
//		Assert.assertEquals(appController.deleteEmployee("123"), "redirect:/list");
//	}

	public List<User> getUsersList() {
		
		final List<User> users = new ArrayList<User>();
		
		final User user1 = new User();
		user1.setId(1l);
		user1.setLogin("manu");
		user1.setPassword("pass4manu");
		user1.setEmail("manu@xyz.com");
		user1.setNom("Mura");
		user1.setPassword("Manu");
		
		final User user2 = new User();
		user2.setId(2l);
		user2.setLogin("johnny");
		user2.setPassword("pass4johnny");
		user2.setEmail("johnny@xyz.com");
		user2.setNom("John");
		user2.setPassword("Doe");
		
		users.add(user1);
		users.add(user2);
		
		return users;
	}
}
