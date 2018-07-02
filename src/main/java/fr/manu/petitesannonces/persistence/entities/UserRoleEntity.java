package fr.manu.petitesannonces.persistence.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import fr.manu.petitesannonces.dto.enums.UserRoleType;

@Entity
@Table(name = "USER_ROLE")
public class UserRoleEntity {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_ID", updatable = false, nullable = false, unique = true)
	private Long id;	

	@Column(name = "USER_ROLE_TYPE", length = 15, unique = true, nullable = false)
	private String type = UserRoleType.USER.getUserProfileType();
	
	@ManyToMany(mappedBy="userRoles", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<UserEntity>(0);
	
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the users
	 */
	public Set<UserEntity> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserRoleEntity))
			return false;
		UserRoleEntity other = (UserRoleEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
        return "UserRoleEntity [id=" + id + ", type=" + type + "]";
	}

}