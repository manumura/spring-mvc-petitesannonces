/**
 * 
 */
package fr.manu.petitesannonces.web.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Manu
 *
 */
@Component
public class SocialFacebookProperties {

    private final String facebookAppId;
	 
    private final String facebookAppSecret;
 
    private final String facebookScope;
 
    @Autowired
    public SocialFacebookProperties(@Value("${facebook.app.id}") String facebookAppId,
            @Value("${facebook.app.secret}") String facebookAppSecret,
            @Value("${facebook.scope}") String facebookScope) {
 
        this.facebookAppId = facebookAppId;
        this.facebookAppSecret = facebookAppSecret;
        this.facebookScope = facebookScope;
    }

    /**
     * @return the facebookAppId
     */
    public String getFacebookAppId() {
        return facebookAppId;
    }

    /**
     * @return the facebookAppSecret
     */
    public String getFacebookAppSecret() {
        return facebookAppSecret;
    }

    /**
     * @return the facebookScope
     */
    public String getFacebookScope() {
        return facebookScope;
    }

}
