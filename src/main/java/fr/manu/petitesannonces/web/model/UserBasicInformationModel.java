package fr.manu.petitesannonces.web.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Manu
 *
 */
public class UserBasicInformationModel implements Serializable {

    private static final long serialVersionUID = 2628824492268867264L;

    private Long id;

    @NotEmpty(message = "{error.message.user.email.not.empty}")
    @Email(message = "{error.message.user.email.incorrect}")
	private String email;

    @NotEmpty(message = "{error.message.user.confirm.email.not.empty}")
    @Email(message = "{error.message.user.confirm.email.incorrect}")
	private String confirmEmail;

    @NotEmpty(message = "{error.message.user.nom.not.empty}")
	private String nom;

    @NotEmpty(message = "{error.message.user.prenom.not.empty}")
	private String prenom;

    // @NotNull(message = "{error.message.user.date.naissance.not.empty}")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateNaissance;

    @Size(max = 50, message = "{error.message.user.ville.size}")
	private String ville;

    @Max(value = 99999, message = "{error.message.user.code.postal.size}")
	private Integer codePostal;

    @Size(max = 60, message = "{error.message.user.adresse.principal.size}")
	private String adressePrincipal;
	
    @Size(max = 80, message = "{error.message.user.adresse.detail.size}")
	private String adresseDetail;

    @Size(max = 30, message = "{error.message.user.pays.size}")
	private String pays;

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
	 * @return the confirmEmail
	 */
	public String getConfirmEmail() {
		return confirmEmail;
	}

	/**
	 * @param confirmEmail the confirmEmail to set
	 */
	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
        return "UserBasicInformationModel [id=" + id + ", email=" + email + ", confirmEmail="
                + confirmEmail + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance="
                + dateNaissance + ", ville=" + ville + ", codePostal=" + codePostal
                + ", adressePrincipal=" + adressePrincipal + ", adresseDetail=" + adresseDetail
                + ", pays=" + pays + "]";
	}

}