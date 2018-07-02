package fr.manu.petitesannonces.dto.enums;

/**
 * 
 * @author Manu
 *
 */
public enum UserRoleType implements EnumeratedLabel<String> {
	
	USER("USER"),
	
	ADMIN("ADMIN");
	
	private String userProfileType;
	
	private UserRoleType(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}

	@Override
	public String getLabel() {
		return userProfileType;
	}
	
}
