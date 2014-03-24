package epic.easystock;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;

import epic.easystock.itemjdoendpoint.Itemjdoendpoint;
import epic.easystock.itemjdoendpoint.model.ItemJDO;


public class TestAddToProductListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
		
		//GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(this, "server:client_id:your_web_client_id");
		Itemjdoendpoint.Builder builder = new Itemjdoendpoint.Builder(
				AndroidHttp.newCompatibleTransport(),
				new GsonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		builder.setApplicationName("my-stock-manager");
		Itemjdoendpoint endpoint = builder.build();
		
		try {
			ItemJDO item = new ItemJDO();
			item.setEmail("fabio@gmail.com");
			item.setAmount(123l);
			item.setName("batata");
			item.setType("typr raiz");
			
			endpoint.insertItemJDO(item);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
