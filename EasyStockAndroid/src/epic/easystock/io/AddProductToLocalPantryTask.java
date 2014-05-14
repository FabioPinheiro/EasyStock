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
		if(fail_product_already_in_the_pantry) {
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_PRODUCT_ALREADY_IN_THE_PANTRY);
		}else if (error_no_pantryDB_createProduct){
			EndPointCall.msg(LOG_TAG, "error_no_pantryDB_createProduct"); //FIXME TEXT
		}else if(result == null){
			Log.e(LOG_TAG, "No fails and result == null    why?");
			EndPointCall.msg(LOG_TAG, EndPointCall.ERROR + 2); //WTF se isto acontecer!!
		}else{
			adapter.add(result);
			Log.i(LOG_TAG, "PROTUDCT_ADDED"); //FIXME TEXT
			EndPointCall.msg(LOG_TAG, EndPointCall.PRODUCT_ADDED_TO_LOCAL_PANTRY);
		}
	}
	@Override
	protected LocalMetaProduct doInBackground(Void... v) {
		List<LocalMetaProduct> newListInLocalPantry = pantryDB.getAllProducts();
		//LIXO FIXME Product newProd = EndPointCall.getApiEndpoint().getProductByBarCode(productBarCode).execute(); //FIXME !!!
		for (LocalMetaProduct mp : newListInLocalPantry) {
			if (mp.getKey().equals(product.getKey())) {
				Log.e(LOG_TAG, EndPointCall.FAIL_PRODUCT_ALREADY_IN_THE_PANTRY);
				fail_product_already_in_the_pantry = true;
				Log.e(LOG_TAG, "Product NOT added to pantry"); //FIXME TEXT
				break;
			}
		}
		if (!fail_product_already_in_the_pantry) {
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
