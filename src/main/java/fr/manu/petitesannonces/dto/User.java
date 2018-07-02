package fr.manu.petitesannonces.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import fr.manu.petitesannonces.dto.enums.SocialMediaProvider;

/**
 * @author Manu
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = -536100632384391231L;

	private Long id;

//	@NotEmpty(message = "user.sessionId.notEmpty")
//	private String ssoId;

	private String login;

	private String password;

	private String email;

	private String nom;

	private String prenom;

	private LocalDate dateNaissance;

	private String ville;

	private Integer codePostal;

	private String adressePrincipal;
	
	private String adresseDetail;

	private String pays;

	private Boolean emailConfirmed;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dateInscription;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime lastAction;

	private Boolean deleted;
	
    private SocialMediaProvider signInProvider;

    private String signInProviderUserId;

    // @NotEmpty
	private List<UserRole> userRoles = new ArrayList<UserRole>();
	
	private Long version;
	
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
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(List<UserRole> userRoles) {
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
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", email=" + email + ", nom=" + nom
				+ ", prenom=" + prenom + ", dateNaissance=" + dateNaissance + ", ville=" + ville + ", codePostal="
				+ codePostal + ", adressePrincipal=" + adressePrincipal + ", adresseDetail=" + adresseDetail + ", pays="
				+ pays + ", emailConfirmed=" + emailConfirmed + ", dateInscription=" + dateInscription + ", lastAction="
				+ lastAction + ", deleted=" + deleted + ", signInProvider=" + signInProvider + ", signInProviderUserId="
				+ signInProviderUserId + ", userRoles=" + userRoles + ", version=" + version + "]";
	}
	
}