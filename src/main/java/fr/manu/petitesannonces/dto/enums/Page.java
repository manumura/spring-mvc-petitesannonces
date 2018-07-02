/**
 * 
 */
package fr.manu.petitesannonces.dto.enums;

/**
 * @author Manu
 *
 */
public enum Page implements EnumeratedLabel<String> {

	LOGIN("login"),
	
	REGISTER("register"),
	
	ERROR("error"),
	
	REGISTER_SUCCESS("register-success"),
	
	ACCESS_DENIED("access-denied"),
	
	USER_EDIT("user/user-edit"),
	
	USER_EDIT_SUCCESS("user/user-edit-success"),
	
	USER_LIST("user/user-list"),
	
    USER_AUTHENTICATION_HEADER("user/user-authentication-header"),

    REGISTRATION_CONFIRMATION_ERROR("registration-confirmation-error"),

    REGISTRATION_CONFIRMATION_SUCCESS("registration-confirmation-success"),

    RESET_PASSWORD("reset-password"),

    CHANGE_PASSWORD("change-password"),

    CHANGE_PASSWORD_ERROR("change-password-error"),
    
    CHANGE_PASSWORD_SUCCESS("change-password-success");

	private String url;
	
	private Page(String url){
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	@Override
	public String getLabel() {
		return url;
	}
	
}
