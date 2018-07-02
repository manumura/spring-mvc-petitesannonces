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
public class SocialProperties {

    private final Boolean isImplicitSignp;
	 
    @Autowired
    public SocialProperties(@Value("${implicit.sign.up}") Boolean isImplicitSignp) {
 
        this.isImplicitSignp = isImplicitSignp;
    }

	/**
	 * @return the isImplicitSignp
	 */
	public Boolean getIsImplicitSignp() {
		return isImplicitSignp;
	}

}
