package epic.easystock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ShoppingList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long key;
	
	private Date TimeStamp;
	
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shoppinglist", cascade = CascadeType.ALL)
	private List<MetaProduct> metaProducts;

	
	public ShoppingList(String name){
		this.setName(name);
		this.metaProducts = new ArrayList<MetaProduct>();
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public List<MetaProduct> getMetaProducts() {
		return metaProducts;
	}


	public void setMetaProducts(List<MetaProduct> metaProducts) {
		this.metaProducts = metaProducts;
	}


	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return TimeStamp;
	}


	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
