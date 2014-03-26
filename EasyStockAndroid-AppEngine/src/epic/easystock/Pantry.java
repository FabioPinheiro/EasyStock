package epic.easystock;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class Pantry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private Date TimeStamp;
	
	@OneToMany(mappedBy = "Pantry", cascade = CascadeType.ALL)
	private List<MetaProduct> products;

	public Date getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}
	
	
	public List<MetaProduct> getProducts() {
		return products;
	}

	public void setProducts(List<MetaProduct> products) {
		this.products = products;
	}

	public Key getKey() {
		return key;
	}
	
}
