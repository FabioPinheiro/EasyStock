package epic.easystock.data;

import java.sql.Timestamp;

import epic.easystock.io.EndPointCall;
import android.content.ContentValues;
import android.database.Cursor;



public class LocalMetaProduct extends LocalObject{
	private final LocalProduct localProduct;
	private Double amount;
	
	public LocalMetaProduct(Cursor cursor) {
		super(cursor);
		long aux = cursor.getLong(cursor.getColumnIndex(STR_MP_LONG_PRODUCT_KEY));
		if(aux == 0 ) throw new RuntimeException(); //FIXME REMOVE
		this.localProduct = EndPointCall.getProductsDbAdapter().getProductByKey(aux); //FIXME isto pode ser adiado para mais trade
		this.amount = cursor.getDouble(cursor.getColumnIndex(STR_MP_REAL_AMOUNT));
	}
	public LocalMetaProduct(LocalProduct localProduct, double amount) {
		super(localProduct);
		//long aux = localProduct.getKey();
		//if(aux == 0 ) throw new RuntimeException(); 
		this.localProduct = localProduct;//FIXME REMOVE EndPointCall.getProductsDbAdapter().getProductByKey(aux); //FIXME isto pode ser adiado para mais trade
		this.amount = amount;
		if(localProduct.getKey() == getKey() || getKey() == 0)
			throw new RuntimeException();//FIXME REMOVE
	}
	/*public LocalMetaProduct(Long localMetaProductKey, LocalProduct localProduct, Double amount, DataState dataState) {
		super(localMetaProductKey, dataState);
		this.localProduct = localProduct;
		this.amount = amount;
	}*/
	@Override
	protected ContentValues getContentValues() {
		ContentValues aux = super.getContentValues();
		aux.put(STR_MP_REAL_AMOUNT, this.getAmount());
		aux.put(STR_MP_LONG_PRODUCT_KEY, this.localProduct.getKey());
		return aux;
	}
	public Double getAmount() {
		return amount;
	}
	public final Long getBarCode() {
		return this.localProduct.getBarCode();
	}
	public final String getName() {
		return this.localProduct.getName();
	}
	public final String getDescription() {
		return this.localProduct.getDescription();
	}
//	public void setBarcode(Long barcode) {
//		localProduct.setBarcode(barcode);
//	}
//	public void setName(String name) {
//		localProduct.setName(name);
//	}
//	public void setDescription(String description) {
//		localProduct.setDescription(description);
//	}
	public void setAmount(Double amount) {
		modified();
		this.amount = amount;
	}
	@Override
	public String toString() {
		return localProduct.toString() + "  Amount: " + amount;
	}
}
