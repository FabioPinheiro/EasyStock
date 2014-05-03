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

public class ListPantryProductTask extends AsyncTask<Context, Integer, List<LocalMetaProduct>> {
	static private final String LOG_TAG = AddProductToPantryTask.class.getCanonicalName();
	
	private MetaProductAdapter adapter;
	private PantryDbAdapter dbAdapter;
	private String pantryID;
	private Boolean pantryLoaded;
	
	public ListPantryProductTask(MetaProductAdapter adapter, String pantryID) {
		this.adapter = adapter;
		this.pantryID = pantryID;
		this.dbAdapter = EndPointCall.getPantryDbAdapter();
	}
	@Override
	protected void onPostExecute(List<LocalMetaProduct> result) {
		super.onPostExecute(result);
		if (pantryLoaded) {
			EndPointCall.msg(LOG_TAG , EndPointCall.FAIL_TO_LOAD_PANTRY);
			Log.e(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PANTRY);
		}
		if (result != null) {
			Collection<LocalMetaProduct> localProducts = result;
			dbAdapter.putAllProducts(localProducts);
			adapter.addAll(localProducts);
		} else {
			Log.e(LOG_TAG, "EndPointCall.FAIL_TO_LIST_PANTRY_PRODUCTS");
			EndPointCall.msg(EndPointCall.FAIL_TO_LIST_PANTRY_PRODUCTS);
		}
	}
	@Override
	protected List<LocalMetaProduct> doInBackground(Context... contexts) {
		
		List<MetaProduct> products = null;
		try {
			Pantry pantry = EndPointCall.getApiEndpoint().getPantryByMailAndName(EndPointCall.getEmailAccount(), pantryID).execute();
			pantryLoaded = (pantry != null);
			if (!pantryLoaded) {
				Log.e(LOG_TAG, EndPointCall.FAIL_TO_LOAD_PANTRY);
			}else {
				products = pantry.getProducts();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (products == null) { //FIXME
			Log.e(LOG_TAG, "products == null");
			products = new ArrayList<MetaProduct>();
		}
		List<LocalMetaProduct> result = new ArrayList<LocalMetaProduct>();
		if (pantryLoaded){
			for (MetaProduct mp : products) {
				Product aux = null;
				try {
					aux = EndPointCall.getApiEndpoint().getProduct(mp.getProduct()).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				result.add(new LocalMetaProduct(aux.getBarCode(), aux
						.getName(), aux.getDescription(), aux.getKey(), mp
						.getAmount()));
			}
		}
		return result;
	}
}