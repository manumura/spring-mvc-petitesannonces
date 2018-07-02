package fr.manu.petitesannonces.persistence.business;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.manu.petitesannonces.persistence.services.RecaptchaAttemptService;

/**
 * @author Manu
 *         http://www.baeldung.com/spring-security-registration-captcha
 *         https://kielczewski.eu/2015/07/spring-recaptcha-v2-form-validation/
 *
 */
@Service("recaptchaAttemptService")
public class RecaptchaAttemptBusiness implements RecaptchaAttemptService {
    
    private static final int MAX_ATTEMPTS = 5;

    private static final int CACHE_TIME_DURATION = 4;

    private LoadingCache<String, Integer> attemptsCache;

    // TODO : logs
    private static final Logger logger = LoggerFactory.getLogger(RecaptchaAttemptBusiness.class);

    public RecaptchaAttemptBusiness() {
        super();
        attemptsCache =
                CacheBuilder.newBuilder().expireAfterWrite(CACHE_TIME_DURATION, TimeUnit.HOURS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.manu.petitesannonces.persistence.services.RecaptchaAttemptService#success(java.lang.
     * String)
     */
    @Override
    public void success(String key) {
        attemptsCache.invalidate(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.manu.petitesannonces.persistence.services.RecaptchaAttemptService#failed(java.lang.String)
     */
    @Override
    public void failed(String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.manu.petitesannonces.persistence.services.RecaptchaAttemptService#isBlocked(java.lang.
     * String)
     */
    @Override
    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= MAX_ATTEMPTS;
    }

}
