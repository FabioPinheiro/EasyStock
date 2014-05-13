package epic.easystock;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Key;

/*User - Pantry (1) - List of all pantries in the system and connects each User with the pantries he has access*/
@Entity
public class UserPantry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	@OneToOne(cascade=CascadeType.ALL) 
	private Long pantry;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Long user;
	
	private PermissionType permissionType;

	public Long getPantry() {
		return pantry;
	}

	public void setPantry(Long pantry) {
		this.pantry = pantry;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	public Key getKey() {
		return key;
	}
	
}
