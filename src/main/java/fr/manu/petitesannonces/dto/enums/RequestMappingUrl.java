package fr.manu.petitesannonces.dto.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Manu
 *
 */
public enum RequestMappingUrl implements EnumeratedLabel<String> {
	
    LOGIN("/login"),
    
    LOGIN_AUTHENTICATE("/login/authenticate"),

    LOGIN_ERROR("/login?error"),

    LOGIN_FAILURE("/login-failure"),

    LOGIN_EXPIRED("/login?expired"),
    
    ACCESS_DENIED("/access-denied"),
    
    ERROR("/error"),

    REGISTER("/register"),
    
    LOGOUT("/logout"),

    LOGOUT_SUCESSFUL("/login?logout"),

    ADMIN_LIST("/admin/list"),

    USER_EDIT("/user/edit"),
    
    RESET_PASSWORD("/resetPassword"),
    
    CHANGE_PASSWORD("/changePassword"),
    
    CHANGE_PASSWORD_SUCCESS("/changePasswordSuccess"),

    CHANGE_PASSWORD_ERROR("/changePasswordError"),

    UPDATE_PASSWORD("/updatePassword"),
    
    RESEND_PASSWORD_RESET_LINK("/resendPasswordResetLink"),
    
    REGISTRATION_CONFIRMATION("/registrationConfirmation"),

    REGISTRATION_CONFIRMATION_ERROR("/registrationConfirmationError"),

    REGISTRATION_CONFIRMATION_SUCCESS("/registrationConfirmationSuccess"),
    
    RESEND_REGISTRATION_CONFIRMATION_LINK("/resendRegistrationConfirmationLink"), 
    
    SIGIN("/signin"), 
    
    SOCIAL_CONNECT("/social/connect/"),

    STATUS("/status"),
    
    FACEBOOK("/facebook"),
    
    TWITTER("/twitter"),
    
    GOOGLE("/google");

	private String url;
	
    private RequestMappingUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	public String getAbsoluteUrl() {
		return StringUtils.replaceOnce(url, "/", "");
	}

	@Override
	public String getLabel() {
		return url;
	}

}
