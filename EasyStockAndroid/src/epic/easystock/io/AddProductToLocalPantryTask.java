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
import epic.easystock.data.PantryDbAdapter;

//FIXME isto tem muito problemasde syscronização(falar com ofabio)
public class AddProductToLocalPantryTask extends AsyncTask<Void, Integer, LocalMetaProduct> {
	static private final String LOG_TAG = AddProductToLocalPantryTask.class.getCanonicalName();

	private MetaProductAdapter adapter;
	private String mail, pantrySelected;
	private boolean fail_product_already_in_the_pantry;
	private boolean error = false;
	private long productId;

	public AddProductToLocalPantryTask(MetaProductAdapter adapter, String pantrySelected, long productId) {
		this.adapter = adapter;
		this.mail = EndPointCall.getEmailAccount();
		this.pantrySelected = pantrySelected;
		this.productId = productId;
		
	}

	@Override
	protected void onPostExecute(LocalMetaProduct result) {
		super.onPostExecute(result);
		if(error == false)
			EndPointCall.msg(LOG_TAG, EndPointCall.ERROR);
		else if(fail_product_already_in_the_pantry) {
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_PRODUCT_ALREADY_IN_THE_PANTRY);
		}else if(result == null){
			Log.e(LOG_TAG, "No fails and result == null    why?");
			EndPointCall.msg(LOG_TAG, EndPointCall.ERROR); //WTF se isto acontecer!!
		}else{
			adapter.add(result);
			EndPointCall.msg(LOG_TAG, EndPointCall.PRODUCT_ADDED_TO_LOCAL_PANTRY);
		}
	}
	@Override
	protected LocalMetaProduct doInBackground(Void... v) {
		try {
			PantryDbAdapter pantryDB = EndPointCall.getPantryDbAdapter();
			List<LocalMetaProduct> newList = pantryDB.getAllProducts();
			Product newProd = EndPointCall.getApiEndpoint().getProductByBarCode(productId).execute(); //FIXME !!!
			for (LocalMetaProduct mp : newList) {
				if (mp.getId().equals(newProd.getKey().longValue())) {
					Log.e(LOG_TAG, EndPointCall.FAIL_PRODUCT_ALREADY_IN_THE_PANTRY);
					fail_product_already_in_the_pantry = true;
					break;
				}
			}
			if (!fail_product_already_in_the_pantry) {
				LocalMetaProduct metaP = new LocalMetaProduct(newProd, 1D);
				newList.add(metaP);//FIXME
				pantryDB.putAllProducts(newList);
				Log.i(LOG_TAG,"Product added to pantry"); //FIXME TEXT
				return metaP;
			}else{
				Log.e(LOG_TAG, "Product NOT added to pantry"); //FIXME TEXT
			}
		} catch (IOException e) {
			error = true;
			e.printStackTrace();
		}
		return null;
	}

}
