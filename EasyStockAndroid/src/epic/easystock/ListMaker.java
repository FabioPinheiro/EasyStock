/**
 * 
 */
package epic.easystock;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.productendpoint.Productendpoint;
import epic.easystock.productendpoint.model.CollectionResponseProduct;
import epic.easystock.productendpoint.model.Product;

/**
 * @author artur
 * 
 */
public class ListMaker {

	public List<Product> getProductList() throws IOException {
		Productendpoint.Builder endpointBuilder = new Productendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		Productendpoint endpoint = CloudEndpointUtils.updateBuilder(
				endpointBuilder).build();
		
		CollectionResponseProduct a = endpoint.listProduct().execute();
		return a.getItems();
		
	}

}
