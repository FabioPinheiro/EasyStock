package epic.easystock.server.components;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Item {
	
	@Persistent
	private String name;
	
	@Persistent	
	private String type;
	
	@Persistent
	private Date dateAdded;
	
	@Persistent
	private Long amount;

	/**
	 * @param name
	 * @param type
	 * @param dateAdded
	 * @param amount
	 */
	public Item(String name, String type, Long amount) {
		super();
		this.name = name;
		this.type = type;
		this.dateAdded = new Date();
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getDateAdded() {
		return dateAdded;
	}
	
	
}
