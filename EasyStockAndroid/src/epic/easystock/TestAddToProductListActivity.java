package epic.easystock;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;


public class TestAddToProductListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
		
		/*//GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(this, "server:client_id:your_web_client_id");
		Itemjdoendpoint.Builder builder = new Itemjdoendpoint.Builder(
				AndroidHttp.newCompatibleTransport(),
				new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		//builder.getGoogleClientRequestInitializer();
		//builder.setApplicationName("my-stock-manager");
		Itemjdoendpoint endpoint = builder.build();
		//endpoint.getGoogleClientRequestInitializer();
		
		try {
			ItemJDO item = new ItemJDO();
			item.setEmail("fabio@gmail.com");
			item.setAmount(123l);
			item.setName("batata");
			item.setType("typr raiz");
			
			endpoint.insertItemJDO(item);
			endpoint.updateItemJDO(item);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
