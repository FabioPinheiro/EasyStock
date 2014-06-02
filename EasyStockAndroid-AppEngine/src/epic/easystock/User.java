package epic.easystock;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}
	
	private String nick;
	private String email;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<UserPantry> userPantriesList;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public List<UserPantry> getUserPantriesList() {
		return userPantriesList;
	}
	
	public void setUserPantriesList(List<UserPantry> userPantriesList) {
		this.userPantriesList = userPantriesList;
	}
	
	public void addUserPantry(UserPantry userPantry) {
		List<UserPantry> aux = getUserPantriesList();
		aux.add(userPantry);
		setUserPantriesList(aux);
	}
}
