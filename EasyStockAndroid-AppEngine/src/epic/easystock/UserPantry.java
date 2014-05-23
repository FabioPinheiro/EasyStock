package epic.easystock;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

/*User - Pantry (1) - List of all pantries in the system and connects each User with the pantries he has access*/
@Entity
public class UserPantry {
	public Key getKey() {
		return key;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	@Unowned //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Pantry pantry; // FIXME
	@Unowned //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Key userKey;
	private PermissionType permissionType; // TODO
	private Date userPantryTimeStamp; // TODO
	
	public Pantry getPantry() {
		return pantry;
	}
	
	public void setPantry(Pantry pantry) {
		this.pantry = pantry;
	}
	
	public Key getUserKey() {
		return userKey;
	}
	
	public void setUserKey(Key userKey) {
		this.userKey = userKey;
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
