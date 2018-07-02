/**
 * 
 */
package fr.manu.petitesannonces.persistence.business;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import fr.manu.petitesannonces.dto.RecaptchaResponse;
import fr.manu.petitesannonces.persistence.exceptions.impl.RecaptchaServiceException;
import fr.manu.petitesannonces.persistence.services.RecaptchaAttemptService;
import fr.manu.petitesannonces.persistence.services.RecaptchaService;
import fr.manu.petitesannonces.web.properties.RecaptchaProperties;

/**
 * @author Manu
 *         http://www.baeldung.com/spring-security-registration-captcha
 *         https://kielczewski.eu/2015/07/spring-recaptcha-v2-form-validation/
 *
 */
@Service("recaptchaService")
public class RecaptchaBusiness implements RecaptchaService {
    
    @Autowired
    private RecaptchaAttemptService recaptchaAttemptService;

    @Autowired
	private RecaptchaProperties recaptchaProperties;

    @Autowired
    @Qualifier("restTemplate")
    private RestOperations restTemplate;
    
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaBusiness.class);

    /*
     * (non-Javadoc)
     * 
     * @see fr.manu.petitesannonces.persistence.services.RecaptchaService#isResponseValid(java.lang.
     * String, java.lang.String)
     */
    @Override
    public boolean isResponseValid(final String remoteIp, final String response)
            throws RecaptchaServiceException {

        if (!responseSanityCheck(response)) {
            logger.error(">>>>> Response contains invalid characters <<<<<");
            return false;
        }

        if (recaptchaAttemptService.isBlocked(remoteIp)) {
            logger.error(">>>>> Client exceeded maximum number of failed attempts <<<<<");
            return false;
        }

        try {
            final RecaptchaResponse recaptchaResponse = restTemplate.postForEntity(
                    recaptchaProperties.getRecaptchaUrl(),
                    createBody(recaptchaProperties.getRecaptchaSecretKey(), remoteIp, response),
                    RecaptchaResponse.class).getBody();
            logger.debug("Google's recaptcha response: {} ", recaptchaResponse.toString());

//			recaptchaResponse.setSuccess(false);
//			recaptchaResponse
//					.setErrorCodes(new RecaptchaResponse.ErrorCode[] { RecaptchaResponse.ErrorCode.INVALID_RESPONSE });

            if (!recaptchaResponse.isSuccess()) {
                if (recaptchaResponse.hasClientError()) {
                    logger.error(">>>>> Service returned client error <<<<<");
                    recaptchaAttemptService.failed(remoteIp);
                }
                logger.error(">>>>> reCaptcha was not successfully validated <<<<<");
                return false;
            }

            recaptchaAttemptService.success(remoteIp);

            return recaptchaResponse.isSuccess();

        } catch (RestClientException rce) {
            logger.error(">>>>> RestClientException <<<<<");
            throw new RecaptchaServiceException(
                    "reCaptcha service unavailable at this time. Please try again later.", rce);
        }
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    private MultiValueMap<String, String> createBody(final String secret, final String remoteIp, final String response) {
    	final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }

}
