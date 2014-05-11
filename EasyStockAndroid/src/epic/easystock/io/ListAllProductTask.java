package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.drive.internal.p;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import epic.easystock.activitys.ProductListActivity;
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalProduct;

public class ListAllProductTask extends AsyncTask<Void, Void, List<LocalProduct>> {
	private enum FROM { NONE, APP_ENGINE,LOCAL_DATA_BASE};
	private final String LOG_TAG = this.getClass().getCanonicalName();
	
	private ProductAdapter adapter;
	private FROM gottedFrom = FROM.NONE;
	public ListAllProductTask(ProductAdapter adapter) {
		this.adapter = adapter;
	}
	@Override
	protected void onPostExecute(List<LocalProduct> result) {
		super.onPostExecute(result);
		if(gottedFrom == FROM.NONE){
			EndPointCall.msg(EndPointCall.FAIL_TO_LIST_PRODUCTS);
		}else{
			if(result == null)
				throw new RuntimeException(); //FIXME REMOVE 
			if (gottedFrom == FROM.LOCAL_DATA_BASE) {
				//NONE !! Update datastore in the future
			}else if (gottedFrom == FROM.APP_ENGINE) {
				EndPointCall.getProductsDbAdapter().putAllProducts(result);
				EndPointCall.msg("AQUI size" + result.size());
			}
			if(adapter != null){
				adapter.addAll(result);
				EndPointCall.msg(EndPointCall.DONE);
			}
		}
	}
	@Override
	protected List<LocalProduct> doInBackground(Void... params) {
		List<LocalProduct> result = new ArrayList<LocalProduct>();
		if(EndPointCall.isConnected()){
			gottedFrom = FROM.APP_ENGINE;
			try {
				List<Product> aux = EndPointCall.getApiEndpoint().listProduct().execute().getItems();
				for (Product product : aux) {
					result.add(new LocalProduct(product));
				}
			} catch (IOException e) {
				e.printStackTrace();//FIXME REMOVE
				Log.e(LOG_TAG, " IOException ", e);
			}
		}else{
			gottedFrom = FROM.LOCAL_DATA_BASE;
			return EndPointCall.getProductsDbAdapter().getAllProducts();
		}
		return result;
	}
}
