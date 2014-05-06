package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.PantryDbAdapter;

public class ListPantryProductTask extends AsyncTask<Context, Integer, List<LocalMetaProduct>> {
	static private final String LOG_TAG = AddProductToPantryTask.class.getCanonicalName();
	private MetaProductAdapter adapter;
	private PantryDbAdapter dbAdapter;
	private String pantryID;
	private Boolean pantryLoaded;
	private Boolean productLoaded;
	
	public ListPantryProductTask(MetaProductAdapter adapter, String pantryID) {
		this.adapter = adapter;
		this.pantryID = pantryID;
		this.dbAdapter = EndPointCall.getPantryDbAdapter();
	}
	@Override
	protected void onPostExecute(List<LocalMetaProduct> result) {
		super.onPostExecute(result);
		if (!pantryLoaded) {
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PANTRY);
		}
		if (!productLoaded) {
			EndPointCall.msg(LOG_TAG ,EndPointCall.FAIL_TO_LIST_PANTRY_PRODUCTS);
		}
		if (result != null) {
			Collection<LocalMetaProduct> localProducts = result;
			if (result.isEmpty()){
				EndPointCall.msg(LOG_TAG ,EndPointCall.PANTRY_IS_EMPTY);
			}
			dbAdapter.putAllProducts(localProducts);
			adapter.addAll(localProducts);
		}else {
			EndPointCall.msg(LOG_TAG ,EndPointCall.ERROR);
			Log.e(LOG_TAG, EndPointCall.ERROR);
		}
	}
	@Override
	protected List<LocalMetaProduct> doInBackground(Context... contexts) {
		Pantry pantry;
		try {
			pantry = EndPointCall.getApiEndpoint().getPantryByMailAndName(EndPointCall.getEmailAccount(), pantryID).execute();
			pantryLoaded = true;
		} catch (IOException e) {
			pantryLoaded = false;
			pantry = null;
			Log.e(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PANTRY);
			e.printStackTrace();
		}
		List<LocalMetaProduct> result = new ArrayList<LocalMetaProduct>();
		// if (pantry == null) {
		// Log.e(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PANTRY);
		// }else {
		List<MetaProduct> products = pantry.getProducts();
		if (products == null) { // FIXME
			Log.e(LOG_TAG, "products == null");
			products = new ArrayList<MetaProduct>();
		}
		// if (pantryLoaded){
		productLoaded = false;
		for (MetaProduct mp : products) {
			try {
				Product aux = EndPointCall.getApiEndpoint().getProduct(mp.getProduct()).execute();
				result.add(new LocalMetaProduct(aux.getBarCode(), aux.getName(), aux.getDescription(), aux.getKey(), mp.getAmount()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PRODUCT);
				e.printStackTrace();
				continue;
			}
			productLoaded = true;
		}
		// }
		// }
		return result;
	}
}