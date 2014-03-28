/**
 * 
 */
package epic.easystock;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.productendpoint.Productendpoint;
import epic.easystock.productendpoint.model.CollectionResponseProduct;
import epic.easystock.productendpoint.model.Product;

public class ListMaker {
	List<Product> result;
	ListProductsTask a;
	
	public List<Product> getProductList() throws IOException {
		Productendpoint.Builder endpointBuilder = new Productendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		Productendpoint endpoint = CloudEndpointUtils.updateBuilder(
				endpointBuilder).build();
		
		CollectionResponseProduct ret = endpoint.listProduct().execute();
		return ret.getItems();
		
/*		a = new ListProductsTask();  //Retirar de comentário se se quiser criar uma thread para correr a listagem;
		a.execute();
		wait();
		//return result;*/
	}

	
	public class ListProductsTask extends AsyncTask<Void, Integer, List<Product>> {
		@Override
		protected void onPostExecute(List<Product> resultado) {
			super.onPostExecute(resultado);
			result = resultado;
		}

		@Override
		protected List<Product> doInBackground(Void... contexts) {
			Productendpoint.Builder endpointBuilder = new Productendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});
			Productendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();
				
				CollectionResponseProduct a = null;
				try {
					a = endpoint.listProduct().execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return a.getItems();
		}
	}
}
