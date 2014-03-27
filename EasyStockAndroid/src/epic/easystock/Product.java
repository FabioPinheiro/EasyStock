package epic.easystock;

public class Product {
	private static final int UNDEFINED = -1;
	private Long idProduct;
	private String name;
	private Long barCode;
	private String description;
	private int quantity = UNDEFINED;
	
	public Product(Long idProduct, String name, Long barCode, String description) {
		super();
		this.idProduct = idProduct;
		this.name = name;
		this.barCode = barCode;
		this.description = description;
		this.quantity = UNDEFINED;
	}

	public Long getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBarCode() {
		return barCode;
	}

	public void setBarCode(Long barCode) {
		this.barCode = barCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
