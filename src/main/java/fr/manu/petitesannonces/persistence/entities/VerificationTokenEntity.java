package fr.manu.petitesannonces.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author emmanuel.mura
 *
 */
@Entity
@Table(name = "USER_ACCOUNT_VERIFICATION_TOKEN")
public class VerificationTokenEntity extends AbstractTokenEntity {

    public VerificationTokenEntity() {
        super();
    }

    public VerificationTokenEntity(String token, UserEntity user) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        // this.verified = false;
    }
     
}
