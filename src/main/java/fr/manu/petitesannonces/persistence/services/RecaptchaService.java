/**
 * 
 */
package fr.manu.petitesannonces.persistence.services;

import fr.manu.petitesannonces.persistence.exceptions.impl.RecaptchaServiceException;

/**
 * @author Manu
 *
 */
public interface RecaptchaService {

	boolean isResponseValid(String remoteIp, String response) throws RecaptchaServiceException;
}
