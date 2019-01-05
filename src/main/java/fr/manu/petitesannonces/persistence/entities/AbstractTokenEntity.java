package fr.manu.petitesannonces.persistence.entities;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author emmanuel.mura
 *
 */
@MappedSuperclass
public class AbstractTokenEntity {

    protected static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID", updatable = false, nullable = false, unique = true)
    protected Long id;

    @Column(name = "TOKEN", nullable = false)
    protected String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "TOKEN_USER_ID", referencedColumnName = "USER_ID", nullable = false)
    protected UserEntity user;

    @Column(name = "TOKEN_EXPIRY_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    protected LocalDateTime expiryDate;

    protected LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new LocalDateTime(cal.getTime());
        // return new Date(cal.getTime().getTime());
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the user
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * @return the expiryDate
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Token [id=" + id + ", token=" + token + ", user=" + user + ", expiryDate="
                + expiryDate + "]";
    }
}
