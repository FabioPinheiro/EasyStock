package epic.easystock.data;

public class LocalMetaProduct {

	private Long BarCode;
	private String name;
	private String description;
	private Long id;
	private Double amount;

	/**
	 * @param barcode
	 * @param name
	 * @param description
	 * @param id
	 * @param double1
	 */
	public LocalMetaProduct(Long barcode, String name, String description,
			Long id, Double amount) {
		super();
		BarCode = barcode;
		this.name = name;
		this.description = description;
		this.id = id;
		this.setAmount(amount);
	}

	public Long getBarcode() {
		return BarCode;
	}

	public void setBarcode(Long barcode) {
		BarCode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
