package epic.easystock.data;

import android.content.ContentValues;
import android.database.Cursor;
import epic.easystock.apiEndpoint.model.Product;

public class LocalProduct extends LocalObject{
	private Long barCode;
	private String name;
	private String description;
	
	/*public LocalProduct(Long localProductKey, String name, Long barcode, String description, LocalObject.DataState dataState) {
		super(localProductKey, dataState);
		this.name = name;
		this.barCode = barcode;
		this.description = description;
	}*/
	public LocalProduct(Product product) {
		super(product);
		this.name = product.getName();
		this.barCode = product.getBarCode();
		this.description = product.getDescription();
	}
	protected LocalProduct(long key) {//FIXME HACK
		super(key);
		this.name = "unknown key=" + key;
		this.barCode = -404l; //ERROR
		this.description = "data not local";
	}
	protected LocalProduct(Cursor cursor) {
		super(cursor);
		this.name = cursor.getString(cursor.getColumnIndex(STR_P_STRING_NAME));
		this.barCode = cursor.getLong(cursor.getColumnIndex(STR_P_LONG_BARCODE));
		this.description = cursor.getString(cursor.getColumnIndex(STR_P_STRING_DESCRIPTION));
	}
	@Override
	protected ContentValues getContentValues() {
		ContentValues aux = super.getContentValues();
		aux.put(STR_P_LONG_BARCODE, this.getBarCode());
		aux.put(STR_P_STRING_NAME, this.getName());
		aux.put(STR_P_STRING_DESCRIPTION, this.getDescription());
		return aux;
	}
	
	public Long getBarCode() {
		return barCode;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}
