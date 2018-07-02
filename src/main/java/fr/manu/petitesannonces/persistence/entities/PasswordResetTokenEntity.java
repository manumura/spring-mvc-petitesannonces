package fr.manu.petitesannonces.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author emmanuel.mura
 *
 */
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetTokenEntity extends AbstractTokenEntity {

    public PasswordResetTokenEntity() {
        super();
    }

    public PasswordResetTokenEntity(String token, UserEntity user) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        // this.verified = false;
    }
     
}
