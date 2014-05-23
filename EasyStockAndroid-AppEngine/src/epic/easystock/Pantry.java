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
		this.metaProducts = new ArrayList<MetaProduct>();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long key;
	
	private Date TimeStamp;
	
	private String name;
	
	//@OneToMany(mappedBy = "pantry", cascade = CascadeType.ALL)
	private List<MetaProduct> metaProducts;

	public Date getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}
	
	public PantrySynchronizationDTO synchronizationMetaProducts(PantrySynchronizationDTO pantrySynchronizationDTO){
		//LIXO if (pantrySynchronizationDTO.getPantryTimeStamp().after(this.getTimeStamp())) {}
		boolean alteracoes = false;
		List<MetaProduct> listToSync = pantrySynchronizationDTO.getListMetaProducts();
		for (MetaProduct it : getMetaProducts()) {
			for (MetaProduct itTodync : listToSync) {
				if(it.getProductKey() == itTodync.getProductKey()){
					if(itTodync.getTimeStamp().after(it.getTimeStamp())){
						it.setAmount(itTodync.getAmount());
						it.setTimeStamp(itTodync.getTimeStamp());
						alteracoes = true;
						listToSync.remove(itTodync);
					}else{}
					//FIXME confirmar o break;
				}else{}
			}			
		}
		if(listToSync!=null){
			getMetaProducts().addAll(listToSync);
			alteracoes = true;
		}
		if(alteracoes){
			this.setTimeStamp(new Date());
			pantrySynchronizationDTO.setPantryTimeStamp(this.getTimeStamp());
		}
		//To synch in client side
		List<MetaProduct> listMetaProductsToSynchInClientSide = new ArrayList<MetaProduct>();
		for(MetaProduct it : this.getMetaProducts()){
			if(it.getTimeStamp().after(pantrySynchronizationDTO.getPantryTimeStamp())){
				listMetaProductsToSynchInClientSide.add(it);
			}
		}
		PantrySynchronizationDTO ret = new PantrySynchronizationDTO();
		ret.setPantryKey(this.getKey());
		ret.setPantryTimeStamp(this.getTimeStamp());
		ret.setListMetaProducts(listMetaProductsToSynchInClientSide);
		return ret;
	}
	
	public List<MetaProduct> getMetaProducts() {
		if (metaProducts == null) {
			System.err.print(this.getClass().getCanonicalName() + ": getProducts(): products is null"); //FIXME put in logo
			metaProducts = new ArrayList<MetaProduct>();
		}
		return metaProducts;
	}

	public void setMetaProducts(List<MetaProduct> products) {
		this.metaProducts = products;
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
