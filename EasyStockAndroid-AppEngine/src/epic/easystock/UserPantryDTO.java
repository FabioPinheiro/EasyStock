package epic.easystock;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class UserPantryDTO {
	public Key getKey() {
		return key;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	@ManyToMany(cascade = CascadeType.ALL)
	private User user;
	@ManyToMany(cascade = CascadeType.ALL)
	private Pantry pantry;// FIXME
	private String pantryName;
	private Date pantryTimeStamp;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Pantry getPantry() {
		return pantry;
	}
	public String getPantryName() {
		return pantryName;
	}
	public void setPantryName(String pantryName) {
		this.pantryName = pantryName;
	}
	public Date getPantryTimeStamp() {
		return pantryTimeStamp;
	}
	public void setPantryTimeStamp(Date pantryTimeStamp) {
		this.pantryTimeStamp = pantryTimeStamp;
	}
}
