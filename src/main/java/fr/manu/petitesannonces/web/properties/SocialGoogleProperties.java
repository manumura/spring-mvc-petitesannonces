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
public class SocialGoogleProperties {

    private final String googleAppId;
	 
    private final String googleAppSecret;
 
    private final String googleScope;
 
    @Autowired
    public SocialGoogleProperties(@Value("${google.app.id}") String googleAppId,
            @Value("${google.app.secret}") String googleAppSecret,
            @Value("${google.scope}") String googleScope) {
 
        this.googleAppId = googleAppId;
        this.googleAppSecret = googleAppSecret;
        this.googleScope = googleScope;
    }

    /**
     * @return the googleAppId
     */
    public String getGoogleAppId() {
        return googleAppId;
    }

    /**
     * @return the googleAppSecret
     */
    public String getGoogleAppSecret() {
        return googleAppSecret;
    }

    /**
     * @return the googleScope
     */
    public String getGoogleScope() {
        return googleScope;
    }

}
