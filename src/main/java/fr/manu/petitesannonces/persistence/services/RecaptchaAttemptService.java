package fr.manu.petitesannonces.persistence.services;

/**
 * @author Manu
 *
 */
public interface RecaptchaAttemptService {

    void success(String key);

    void failed(String key);

    boolean isBlocked(String key);
}
