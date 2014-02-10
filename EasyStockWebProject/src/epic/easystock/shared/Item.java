package epic.easystock.shared;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Item {
	@PrimaryKey
	private String name;
	
	@Persistent
	private String email;
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
	public Item(String email, String name, String type, Long amount) {
		super();
		this.email = email;
		this.name = name;
		this.type = type;
		this.dateAdded = new Date();
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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


