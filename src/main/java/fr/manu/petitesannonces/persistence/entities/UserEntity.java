package fr.manu.petitesannonces.persistence.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import fr.manu.petitesannonces.dto.enums.SocialMediaProvider;

/**
 * @author Manu
 *
 */
@Entity
@Table(name = "USER_ACCOUNT")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", updatable = false, nullable = false, unique = true)
	private Long id;
	
//	@Column(name = "USER_SSO_ID", nullable = false, unique = true)
//	private String ssoId;
	
	@Column(name = "USER_LOGIN", nullable = false, unique = true)
	private String login;
	
    @Column(name = "USER_PASSWORD", nullable = false)
	private String password;
	
    @Column(name = "USER_EMAIL", nullable = false, unique = true)
	private String email;
	
    // columnDefinition = "TINYINT", length = 1, BIT, BOOLEAN
    // @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "USER_IS_EMAIL_CONFIRMED", nullable = false)
	private Boolean emailConfirmed;
	
	@Column(name = "USER_LAST_NAME")
	private String nom;
	
	@Column(name = "USER_FIRST_NAME")
	private String prenom;
	
	@Column(name = "USER_DATE_BIRTH")
	@DateTimeFormat(pattern="dd/MM/yyyy") 
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dateNaissance;
	
	@Column(name = "USER_CITY")
	private String ville;
	
	@Column(name = "USER_ZIP_CODE")
	private Integer codePostal;
	
	@Column(name = "USER_ADDRESS_PRINCIPAL")
	private String adressePrincipal;
	
	@Column(name = "USER_ADDRESS_DETAIL")
	private String adresseDetail;
	
	@Column(name = "USER_COUNTRY")
	private String pays;
	
	@Column(name = "USER_DATE_REGISTRATION", nullable = false)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss") 
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime dateInscription;
	
	@Column(name = "USER_LAST_ACTION")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss") 
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime lastAction;
	
    // columnDefinition = "TINYINT", length = 1, BIT, BOOLEAN
    // @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "USER_IS_DELETED", nullable = false)
	private Boolean deleted;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_SIGN_IN_PROVIDER", length = 20)
    private SocialMediaProvider signInProvider;

    @Column(name = "USER_SIGN_IN_PROVIDER_USER_ID", length = 20)
    private String signInProviderUserId;

    @Version
    private Long version;

	@OrderBy("type")
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ACCOUNT_USER_ROLE", 
             joinColumns = { @JoinColumn(name = "USER_ID") }, 
             inverseJoinColumns = { @JoinColumn(name = "USER_ROLE_ID") })
	private Set<UserRoleEntity> userRoles = new HashSet<UserRoleEntity>(0);
	
    @OneToOne(targetEntity = VerificationTokenEntity.class, mappedBy = "user",
            cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private VerificationTokenEntity verificationToken;
	
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailConfirmed
	 */
	public Boolean getEmailConfirmed() {
		return emailConfirmed;
	}

	/**
	 * @param emailConfirmed the emailConfirmed to set
	 */
	public void setEmailConfirmed(Boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	/**
	 * @return the dateInscription
	 */
	public LocalDateTime getDateInscription() {
		return dateInscription;
	}

	/**
	 * @param dateInscription the dateInscription to set
	 */
	public void setDateInscription(LocalDateTime dateInscription) {
		this.dateInscription = dateInscription;
	}

	/**
	 * @return the lastAction
	 */
	public LocalDateTime getLastAction() {
		return lastAction;
	}

	/**
	 * @param lastAction the lastAction to set
	 */
	public void setLastAction(LocalDateTime lastAction) {
		this.lastAction = lastAction;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the dateNaissance
	 */
	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	/**
	 * @param dateNaissance the dateNaissance to set
	 */
	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return the codePostal
	 */
	public Integer getCodePostal() {
		return codePostal;
	}

	/**
	 * @param codePostal the codePostal to set
	 */
	public void setCodePostal(Integer codePostal) {
		this.codePostal = codePostal;
	}

	/**
	 * @return the adressePrincipal
	 */
	public String getAdressePrincipal() {
		return adressePrincipal;
	}

	/**
	 * @param adressePrincipal the adressePrincipal to set
	 */
	public void setAdressePrincipal(String adressePrincipal) {
		this.adressePrincipal = adressePrincipal;
	}

	/**
	 * @return the adresseDetail
	 */
	public String getAdresseDetail() {
		return adresseDetail;
	}

	/**
	 * @param adresseDetail the adresseDetail to set
	 */
	public void setAdresseDetail(String adresseDetail) {
		this.adresseDetail = adresseDetail;
	}

	/**
	 * @return the pays
	 */
	public String getPays() {
		return pays;
	}

	/**
	 * @param pays the pays to set
	 */
	public void setPays(String pays) {
		this.pays = pays;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the userRoles
	 */
	public Set<UserRoleEntity> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(Set<UserRoleEntity> userRoles) {
		this.userRoles = userRoles;
	}

    /**
     * @return the signInProvider
     */
    public SocialMediaProvider getSignInProvider() {
        return signInProvider;
    }

    /**
     * @param signInProvider the signInProvider to set
     */
    public void setSignInProvider(SocialMediaProvider signInProvider) {
        this.signInProvider = signInProvider;
    }

    /**
     * @return the signInProviderUserId
     */
    public String getSignInProviderUserId() {
        return signInProviderUserId;
    }

    /**
     * @param signInProviderUserId the signInProviderUserId to set
     */
    public void setSignInProviderUserId(String signInProviderUserId) {
        this.signInProviderUserId = signInProviderUserId;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    
    /**
	 * @return the verificationToken
	 */
	public VerificationTokenEntity getVerificationToken() {
		return verificationToken;
	}

	/**
	 * @param verificationToken the verificationToken to set
	 */
	public void setVerificationToken(VerificationTokenEntity verificationToken) {
		this.verificationToken = verificationToken;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
        return "UserEntity [id=" + id + ", login=" + login + ", password=" + password + ", email="
                + email + ", emailConfirmed=" + emailConfirmed + ", nom=" + nom + ", prenom="
                + prenom + ", dateNaissance=" + dateNaissance + ", ville=" + ville + ", codePostal="
                + codePostal + ", adressePrincipal=" + adressePrincipal + ", adresseDetail="
                + adresseDetail + ", pays=" + pays + ", dateInscription=" + dateInscription
                + ", lastAction=" + lastAction + ", deleted=" + deleted + ", signInProvider="
                + signInProvider + ", signInProviderUserId=" + signInProviderUserId + ", version="
                + version + ", userRoles=" + userRoles + "]";
    }

}