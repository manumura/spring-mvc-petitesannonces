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
public class SocialTwitterProperties {

    private final String twitterConsumerKey;
	 
    private final String twitterConsumerSecret;
 
    @Autowired
    public SocialTwitterProperties(@Value("${twitter.consumer.key}") String twitterConsumerKey,
            @Value("${twitter.consumer.secret}") String twitterConsumerSecret) {
 
        this.twitterConsumerKey = twitterConsumerKey;
        this.twitterConsumerSecret = twitterConsumerSecret;
    }

    /**
     * @return the twitterConsumerKey
     */
    public String getTwitterConsumerKey() {
        return twitterConsumerKey;
    }

    /**
     * @return the twitterConsumerSecret
     */
    public String getTwitterConsumerSecret() {
        return twitterConsumerSecret;
    }

}
