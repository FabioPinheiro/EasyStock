package epic.easystock.assist;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.CloudEndpointUtils;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;

public final class EndpointCall {

	static private ApiEndpoint.Builder apiEndpointBuilder = null;
	static private ApiEndpoint apiEndpoint = null;
	static{
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

	static public void addProductTask(String name, Long barCode,
			String description) {
		new EndpointsTask()
				.execute(new epic.easystock.assist.EndpointCall.EndpointsTask.Param(
						name, barCode, description));
	}
	
	static public void listProductTask(ProductAdapter adapter){
		new EndpointTask2(adapter).execute();
	}

	// ##################################################################################
	private static class EndpointTask2 extends AsyncTask<Void, Void, List<Product> > {
		private ProductAdapter adapter;
		public EndpointTask2(ProductAdapter adapter) {
			this.adapter = adapter;
		}
		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);
			Collection<Product> aux = result;
			adapter.addAll(aux);
		}

		@Override
		protected List<Product> doInBackground(Void... params) {
			CollectionResponseProduct result = null;
			try {
				result = getApiEndpoint().listProduct().execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null){
				return result.getItems();
			}else{
				return null;
			}
		}

	}

	private static class EndpointsTask
			extends
			AsyncTask<epic.easystock.assist.EndpointCall.EndpointsTask.Param, Integer, Long> {
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
		protected Long doInBackground(Param... params) {
			try {
				// Product productAux = new Product(idProduct, name, barCode,
				// description);

				Product content = new Product();
				content.setName(params[0].name);
				content.setBarCode(params[0].barCode);
				content.setDescription(params[0].description);

				Product result = getApiEndpoint().insertProduct(content).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0; //FIXME
		}
	}
}
