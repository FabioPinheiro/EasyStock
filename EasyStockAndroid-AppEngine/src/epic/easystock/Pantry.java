package epic.easystock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pantry {

	public Pantry(String name){
		this.name = name;
		this.products = new ArrayList<MetaProduct>();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long key;
	
	private Date TimeStamp;
	
	private String name;
	
	//@OneToMany(mappedBy = "pantry", cascade = CascadeType.ALL)
	private List<MetaProduct> products;

	public Date getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}
	
	
	public List<MetaProduct> getProducts() {
		if (products == null) {
			System.err.print(this.getClass().getCanonicalName() + ": getProducts(): products is null"); //FIXME
			products = new ArrayList<MetaProduct>();
		}
		return products;
	}

	public void setProducts(List<MetaProduct> products) {
		this.products = products;
	}

	public Long getKey() {
		return key;
	}

	public void setkey(Long key) { //FIXME REMOVE
		this.key = key;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
