/**
 * 
 */
package fr.manu.petitesannonces.dto.enums;

/**
 * @author Manu
 *
 */
public enum SocialMediaProvider implements EnumeratedLabel<String> {

	FACEBOOK("facebook"),
	
	TWITTER("twitter"),
	
	GOOGLE("google");
	
	private String providerId;
	
	private SocialMediaProvider(String providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	@Override
	public String getLabel() {
		return providerId;
	}
}
