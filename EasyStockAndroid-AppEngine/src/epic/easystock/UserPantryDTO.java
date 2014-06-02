package epic.easystock;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private String email; //FIXME 
	@ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
	private Pantry pantry;// FIXME
	private String pantryName;
	private Date pantryTimeStamp;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String user) {
		this.email = user;
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
