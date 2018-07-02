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
public class RecaptchaProperties {

	private final String recaptchaUrl;
	 
    private final String recaptchaSecretKey;
 
    private final String recaptchaSiteKey;
 
    @Autowired
    public RecaptchaProperties(@Value("${recaptcha.url}") String recaptchaUrl,
                         @Value("${recaptcha.site.key}") String recaptchaSiteKey,
                         @Value("${recaptcha.secret.key}") String recaptchaSecretKey) {
 
        this.recaptchaUrl = recaptchaUrl;
        this.recaptchaSecretKey = recaptchaSecretKey;
        this.recaptchaSiteKey = recaptchaSiteKey;
    }

	/**
	 * @return the recaptchaUrl
	 */
	public String getRecaptchaUrl() {
		return recaptchaUrl;
	}

	/**
	 * @return the recaptchaSecretKey
	 */
	public String getRecaptchaSecretKey() {
		return recaptchaSecretKey;
	}

	/**
	 * @return the recaptchaSiteKey
	 */
	public String getRecaptchaSiteKey() {
		return recaptchaSiteKey;
	}
    
}
