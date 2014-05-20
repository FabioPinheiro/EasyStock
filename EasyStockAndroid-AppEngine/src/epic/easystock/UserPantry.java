package epic.easystock;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;

/*User - Pantry (1) - List of all pantries in the system and connects each User with the pantries he has access*/
@Entity
public class UserPantry {
	public Key getKey() {
		return key;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	@ManyToOne(cascade = CascadeType.ALL)
	private Long pantry; //FIXME 
	
	@ManyToOne(cascade = CascadeType.ALL) //@OneToOne(cascade = CascadeType.ALL)
	private User user; // FIXME isto não devia ser long!!
	private PermissionType permissionType; //TODO
	private Date userPantryTimeStamp; //TODO
	
	public Long getPantry() {
		return pantry;
	}
	
	public void setPantry(Long pantry) {
		this.pantry = pantry;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public PermissionType getPermissionType() {
		return permissionType;
	}
	
	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}
	
	public Date getUserPantryTimeStamp() {
		return userPantryTimeStamp;
	}
	
	public void setUserPantryTimeStamp(Date userPantryTimeStamp) {
		this.userPantryTimeStamp = userPantryTimeStamp;
	}
}
