package epic.easystock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class UserPantryDTO{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	private User user;
	private Pantry pantry;//FIXME
	public String pantryName;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Pantry getPantry() {
		return pantry;
	}
}
