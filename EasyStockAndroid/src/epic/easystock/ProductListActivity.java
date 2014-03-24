package epic.easystock;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;
import java.util.Date;

import android.os.AsyncTask;
import android.widget.EditText;
import android.content.Context;
import epic.easystock.itemendpoint.*;
import epic.easystock.itemendpoint.model.*;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

public class ProductListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		
		new EndpointsTask().execute(getApplicationContext());
	}

	public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
		protected Long doInBackground(Context... contexts) {

			Itemendpoint.Builder endpointBuilder = new Itemendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});
			Itemendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();
			try {
				 
				ItemData item = new ItemData();
				String name = ((EditText)findViewById(R.id.itemName)).getText().toString(); 
				item.setName(name);
				
				String description = ((EditText)findViewById(R.id.descriptionText)).getText().toString();;
				item.setDescription(description);
				ItemData result = endpoint.insertItem(item).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}

}
