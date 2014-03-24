package epic.easystock;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ItemJDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String name;
	
	@Persistent
	private String email;
	
	@Persistent	
	private String type;
	
	@Persistent
	private Long amount;

	public ItemJDO() {
		super();
	}
	
	/**
	 * @param name
	 * @param type
	 * @param dateAdded
	 * @param amount
	 */
	public ItemJDO(String email, String name, String type, Long amount) {
		super();
		this.email = email;
		this.name = name;
		this.type = type;
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
	
	
}


