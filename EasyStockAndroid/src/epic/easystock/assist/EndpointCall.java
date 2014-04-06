package epic.easystock.assist;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.CloudEndpointUtils;
import epic.easystock.activitys.HomeActivity;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;

public final class EndpointCall {

	static private final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static private final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static private final String FAIL_TO_ADD_PRODUCT = "FAIL_TO_ADD_PRODUCT";
	static private final String DONE = "AsyncTask Done"; //FIXME remove!!! use in debug
	
	static private ApiEndpoint.Builder apiEndpointBuilder = null;
	static private ApiEndpoint apiEndpoint = null;
	static private Context globalContext = null;
	
	static public void init(Context applicationContext){
		globalContext = applicationContext;
		apiEndpointBuilder = new ApiEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		apiEndpoint = CloudEndpointUtils.updateBuilder(
				apiEndpointBuilder).build();
	}
	
	public static ApiEndpoint getApiEndpoint() {
		return apiEndpoint;
	}
	public static Context getGlobalContext() {
		return globalContext;
	}
	static private void msg(String message){
		Toast.makeText(EndpointCall.getGlobalContext(), message, Toast.LENGTH_LONG).show();
	}
	static public void addProductTask(String name, Long barCode,
			String description) {
		new AddProductTask()
				.execute(new epic.easystock.assist.EndpointCall.AddProductTask.Param(
						name, barCode, description));
	}
	
	static public void listProductTask(ProductAdapter adapter){
		new ListAllProductTask(adapter).execute();
	}
	
	static public void listPantryProductTask(MetaProductAdapter adapter, Long pantryID){
		new ListPantryProductTask(adapter, pantryID).execute();
	}

	// ##################################################################################
	public static class ListPantryProductTask extends AsyncTask<Context, Integer, List<MetaProduct>> {
		private MetaProductAdapter adapter;
		private Long pantryID;
		
		public ListPantryProductTask(MetaProductAdapter adapter, Long pantryID) {
			this.adapter = adapter;
			this.pantryID = pantryID;
		}
		
		@Override
		protected void onPostExecute(List<MetaProduct> result) {
			super.onPostExecute(result);
			Collection<MetaProduct> aux = result;
			EndpointCall.msg(EndpointCall.DONE);
			if (result != null){
				adapter.addAll(aux);
			}else{;
				EndpointCall.msg(EndpointCall.FAIL_TO_LIST_PANTRY_PRODUCTS);
			}
		}
		
		@Override
		protected List<MetaProduct> doInBackground(Context... contexts) {
			List<MetaProduct> products = null;
			try {
				 products =  EndpointCall.getApiEndpoint().getPantryProducts(pantryID)
						.execute().getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return products;
		}
	}
	
	
	
	private static class ListAllProductTask extends AsyncTask<Void, Void, List<Product> > {
		private ProductAdapter adapter;
		public ListAllProductTask(ProductAdapter adapter) {
			this.adapter = adapter;
		}
		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);
			Collection<Product> aux = result;
			EndpointCall.msg(EndpointCall.DONE);
			if (result != null){
				adapter.addAll(aux);
			}else{;
				EndpointCall.msg(EndpointCall.FAIL_TO_LIST_PRODUCTS);
			}
		}

		@Override
		protected List<Product> doInBackground(Void... params) {
			List<Product> result = null;
			try {
				result = getApiEndpoint().listProduct().execute().getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

	}

	private static class AddProductTask
			extends
			AsyncTask<epic.easystock.assist.EndpointCall.AddProductTask.Param, Integer, Product> {
		static class Param {
			String name;
			Long barCode;
			String description;

			public Param(String name, Long barCode, String description) {
				super();
				this.name = name;
				this.barCode = barCode;
				this.description = description;
			}
		}
		

		@Override
		protected void onPostExecute(Product result) {
			super.onPostExecute(result);
			if (result != null){
				EndpointCall.msg(EndpointCall.DONE);
			}else{;
				EndpointCall.msg(EndpointCall.FAIL_TO_ADD_PRODUCT);
			}
		}


		@Override
		protected Product doInBackground(Param... params) {
			Product result = null;
			try {
				Product content = new Product();
				content.setName(params[0].name);
				content.setBarCode(params[0].barCode);
				content.setDescription(params[0].description);

				result = getApiEndpoint().insertProduct(content).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
}
