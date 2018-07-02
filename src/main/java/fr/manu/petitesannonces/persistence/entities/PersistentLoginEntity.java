package fr.manu.petitesannonces.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PERSISTENT_LOGIN")
public class PersistentLoginEntity {

	@Id
	@Column(name = "SERIES", unique = true, nullable = false)
	private String series;

	@Column(name = "USERNAME", unique = true, nullable = false)
	private String username;
	
	@Column(name = "TOKEN", unique = true, nullable = false)
	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_USED", nullable = false)
	private Date lastUsed;

	/**
	 * @return the series
	 */
	public String getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the lastUsed
	 */
	public Date getLastUsed() {
		return lastUsed;
	}

	/**
	 * @param lastUsed the lastUsed to set
	 */
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PersistentLoginEntity [series=" + series + ", username=" + username + ", token="
                + token + ", lastUsed=" + lastUsed + "]";
    }

}
