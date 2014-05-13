package epic.easystock;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class PantrySynchronizationDTO{

	public Key getKey() {
		return key;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private Long pantryKey;
	private Date pantryTimeStamp;
	
	private List<MetaProduct> listMetaProducts;
	
	public Long getPantryKey() {
		return pantryKey;
	}
	public void setPantryKey(Long pantryKey) {
		this.pantryKey = pantryKey;
	}
	public Date getPantryTimeStamp() {
		return pantryTimeStamp;
	}
	public void setPantryTimeStamp(Date pantryTimeStamp) {
		this.pantryTimeStamp = pantryTimeStamp;
	}
	public List<MetaProduct> getListMetaProducts() {
		return listMetaProducts;
	}
	public void setListMetaProducts(List<MetaProduct> listMetaProducts) {
		this.listMetaProducts = listMetaProducts;
	}
}