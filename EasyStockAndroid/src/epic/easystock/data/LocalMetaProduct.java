package epic.easystock.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.io.EndPointCall;

public class LocalMetaProduct extends LocalObject {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private final LocalProduct localProduct;
	private Double amount;
	
	public static List<MetaProduct> convert(List<LocalMetaProduct> list) {
		List<MetaProduct> ret = new ArrayList<MetaProduct>();
		for (LocalMetaProduct localMetaProduct : list) {
			MetaProduct mp = new MetaProduct();
			mp.setProductKey(localMetaProduct.getKey());
			mp.setTimeStamp(localMetaProduct.getTimestamp());
			mp.setAmount(localMetaProduct.getAmount());
			ret.add(mp);
		}
		return ret;
	}
	
	public LocalMetaProduct(MetaProduct metaProduct) {
		super(metaProduct);
		LocalProduct aux = EndPointCall.getProductsDbAdapter().getProductByKey(metaProduct.getProductKey()); // FIXME
		if (aux == null)
			throw new RuntimeException(); // REMOVE
		if (aux.getBarCode() == -404l)
			Log.d(LOG_TAG, "LocalProduct.getBarCode() == -404l");
		this.localProduct = aux;
		this.amount = metaProduct.getAmount();
	}
	
	public LocalMetaProduct(Cursor cursor) {
		super(cursor);
		long aux = cursor.getLong(cursor.getColumnIndex(STR_MP_LONG_PRODUCT_KEY));
		if (aux == 0)
			throw new RuntimeException(); // FIXME REMOVE
		this.localProduct = EndPointCall.getProductsDbAdapter().getProductByKey(aux); // FIXME isto pode ser adiado para mais trade
		this.amount = cursor.getDouble(cursor.getColumnIndex(STR_MP_REAL_AMOUNT));
	}
	
	public LocalMetaProduct(LocalProduct localProduct, double amount, int i) {
		super(localProduct);
		// long aux = localProduct.getKey();
		// if(aux == 0 ) throw new RuntimeException();
		this.localProduct = localProduct;// FIXME REMOVE EndPointCall.getProductsDbAdapter().getProductByKey(aux); //FIXME isto pode ser adiado para mais trade
		this.amount = amount;
		if (localProduct.getKey() == getKey() || getKey() == 0)
			throw new RuntimeException();// FIXME REMOVE
	}
	
	/*
	 * public LocalMetaProduct(Long localMetaProductKey, LocalProduct localProduct, Double amount, DataState dataState) { super(localMetaProductKey, dataState); this.localProduct = localProduct; this.amount = amount; }
	 */
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
	
	// public void setBarcode(Long barcode) {
	// localProduct.setBarcode(barcode);
	// }
	// public void setName(String name) {
	// localProduct.setName(name);
	// }
	// public void setDescription(String description) {
	// localProduct.setDescription(description);
	// }
	public void setAmount(Double amount) {
		modified();
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return localProduct.toString() + "  Amount: " + amount;
	}
}
