package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalProduct;
import epic.easystock.data.PantriesDBAdapter;
import epic.easystock.data.PantriesDBAdapter.PantryDB;

//FIXME isto tem muito problemasde syscronização(falar com ofabio)
public class AddProductToLocalPantryTask extends AsyncTask<Void, Integer, LocalMetaProduct> {
	static private final String LOG_TAG = AddProductToLocalPantryTask.class.getCanonicalName();

	private MetaProductAdapter adapter;
	private String mail;
	private PantriesDBAdapter.PantryDB pantryDB;
	private boolean fail_product_already_in_the_pantry = false;
	private boolean error_no_pantryDB_createProduct = false;
	//private boolean error_no_pantryDB_updateProduct = true;
	private boolean error_no_pantryDB_updateProduct_key = true;
	private boolean error_no_pantryDB_updateProduct_barCode = true;
	private LocalMetaProduct product;

	public AddProductToLocalPantryTask(MetaProductAdapter adapter, PantriesDBAdapter.PantryDB pantryDB, LocalMetaProduct localMetaProduct) {
		this.adapter = adapter;
		this.mail = EndPointCall.getEmailAccount();
		this.pantryDB = pantryDB;
		this.product = localMetaProduct;
	}


	@Override
	protected void onPostExecute(LocalMetaProduct result) {
		super.onPostExecute(result);
		if(!(error_no_pantryDB_updateProduct_key || error_no_pantryDB_updateProduct_barCode)) {
			if(error_no_pantryDB_updateProduct_key)
				EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_PRODUCT_KEY_ALREADY_IN_THE_PANTRY);
			if(error_no_pantryDB_updateProduct_barCode)
				EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_PRODUCT_BARCODE_ALREADY_IN_THE_PANTRY);
			EndPointCall.msg(LOG_TAG, EndPointCall.PRODUCT_ALREADY_IN_THE_PANTRY_UPDATED);
		}else if (error_no_pantryDB_createProduct){
			EndPointCall.msg(LOG_TAG, "error_no_pantryDB_createProduct"); //FIXME TEXT
		}else if(result == null){
			Log.e(LOG_TAG, "No fails and result == null    why?");
			EndPointCall.msg(LOG_TAG, EndPointCall.ERROR + 3); //WTF se isto acontecer!!
		}else{
			adapter.add(result);
			Log.i(LOG_TAG, "PROTUDCT_ADDED"); //FIXME TEXT
			EndPointCall.msg(LOG_TAG, EndPointCall.PRODUCT_ADDED_TO_LOCAL_PANTRY);
		}
		adapter.clear();
		adapter.addAll(pantryDB.getAllProducts());
	}
	@Override
	protected LocalMetaProduct doInBackground(Void... v) {
		List<LocalMetaProduct> newListInLocalPantry = pantryDB.getAllProducts();
		//LIXO FIXME Product newProd = EndPointCall.getApiEndpoint().getProductByBarCode(productBarCode).execute(); //FIXME !!!
		for (LocalMetaProduct mp : newListInLocalPantry) {
			if (mp.getKey().equals(product.getKey())) {
				//mp.setAmount(mp.getAmount()+1);
				pantryDB.updateProduct(mp);
				Log.e(LOG_TAG, EndPointCall.FAIL_PRODUCT_KEY_ALREADY_IN_THE_PANTRY);
				error_no_pantryDB_updateProduct_key = false;
				//Log.e(LOG_TAG, "Product NOT added to pantry"); //FIXME TEXT
				break;
			}
			if (mp.getBarCode().equals(product.getBarCode())) {
				//mp.setAmount(mp.getAmount()+1);
				pantryDB.updateProduct(mp);
				Log.e(LOG_TAG, EndPointCall.FAIL_PRODUCT_BARCODE_ALREADY_IN_THE_PANTRY);
				error_no_pantryDB_updateProduct_barCode = false;
				//Log.e(LOG_TAG, "Product NOT added to pantry"); //FIXME TEXT
				break;
			}
		}
		if (error_no_pantryDB_updateProduct_key || error_no_pantryDB_updateProduct_barCode) {
			if( -1 != pantryDB.createProduct(product)){
				newListInLocalPantry.add(product);//FIXME
				return product;
			}else{
				error_no_pantryDB_createProduct = true;
				Log.e(LOG_TAG, "error_no_pantryDB_createProduct"); //FIXME TEXT
				return null;
			}
			//FIXME pantryDB.putAllProducts(newList);
		}
		return null;
	}

}
