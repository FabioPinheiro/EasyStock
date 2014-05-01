package epic.easystock.io;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import epic.easystock.activitys.ProductListActivity;
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.ProductAdapter;

public class ListAllProductTask extends AsyncTask<Void, Void, List<Product>> {
	private ProductAdapter adapter;
	
	public ListAllProductTask(ProductAdapter adapter) {
		this.adapter = adapter;
	}
	@Override
	protected void onPostExecute(List<Product> result) {
		super.onPostExecute(result);
		Collection<Product> aux = result;
		EndPointCall.msg(EndPointCall.DONE);
		if (result != null) {
			adapter.addAll(aux);
		} else {
			EndPointCall.msg(EndPointCall.FAIL_TO_LIST_PRODUCTS);
		}
	}
	@Override
	protected List<Product> doInBackground(Void... params) {
		List<Product> result = null;
		try {
			result = EndPointCall.getApiEndpoint().listProduct().execute().getItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
