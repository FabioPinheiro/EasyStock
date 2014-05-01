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
	private MetaProductAdapter adapter;
	private PantryDbAdapter dbAdapter;
	private String pantryID;
	
	public ListPantryProductTask(MetaProductAdapter adapter, String pantryID) {
		this.adapter = adapter;
		this.pantryID = pantryID;
		this.dbAdapter = EndPointCall.getPantryDbAdapter();
	}
	@Override
	protected void onPostExecute(List<LocalMetaProduct> result) {
		super.onPostExecute(result);
		if (result != null) {
			dbAdapter.putAllProducts(result);
			Collection<LocalMetaProduct> localProducts = result;
			adapter.addAll(localProducts);
		} else {
			Log.e(this.getClass().getCanonicalName(), "EndPointCall.FAIL_TO_LIST_PANTRY_PRODUCTS");
			EndPointCall.msg(EndPointCall.FAIL_TO_LIST_PANTRY_PRODUCTS);
		}
	}
	@Override
	protected List<LocalMetaProduct> doInBackground(Context... contexts) {
		
		List<MetaProduct> products = null;
		try {
			Pantry pantry = EndPointCall.getApiEndpoint().getPantryByMailAndName(EndPointCall.getEmailAccount(), pantryID).execute();
			products = pantry.getProducts();
			if (products == null) {
				Log.e("PantyActivity", "ListPantryTask PANTRY "
						+ "products == null");
				products = new ArrayList<MetaProduct>();
				pantry.setProducts(products);
				EndPointCall.getApiEndpoint().updatePantry(pantry).execute();
			}
			//products = EndPointCall.getApiEndpoint().getPantryProducts(pantryID).execute().getItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<LocalMetaProduct> localProducts = new ArrayList<LocalMetaProduct>();
		for (MetaProduct mp : products) {
			Product aux = null;
			try {
				aux = EndPointCall.getApiEndpoint().getProduct(mp.getProduct()).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			localProducts.add(new LocalMetaProduct(aux.getBarCode(), aux
					.getName(), aux.getDescription(), aux.getKey(), mp
					.getAmount()));
		}
		return localProducts;
	}
}