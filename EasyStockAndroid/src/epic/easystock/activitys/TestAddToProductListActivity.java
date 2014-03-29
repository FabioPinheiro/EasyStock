package epic.easystock.activitys;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.CloudEndpointUtils;
import epic.easystock.R;
import epic.easystock.assist.EndpointCall;
import epic.easystock.productendpoint.Productendpoint;

public class TestAddToProductListActivity extends Activity {

	private OnTouchListener addListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
	}

	/*
	 * Button addItemButton = (Button) findViewById(R.id.button1);
	 * 
	 * addListener = new OnTouchListener() {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) {
	 * Itemendpoint.Builder endpointBuilder = new Itemendpoint.Builder(
	 * AndroidHttp.newCompatibleTransport(), new JacksonFactory(), new
	 * HttpRequestInitializer() { public void initialize(HttpRequest
	 * httpRequest) { } }); Itemendpoint endpoint =
	 * CloudEndpointUtils.updateBuilder( endpointBuilder).build(); try {
	 * 
	 * ItemData item = new ItemData(); String name = ((EditText)
	 * findViewById(R.id.itemName)) .getText().toString(); item.setName(name);
	 * 
	 * String description = ((EditText) findViewById(R.id.descriptionText))
	 * .getText().toString(); ; item.setDescription(description); ItemData
	 * result = endpoint.insertItem(item).execute(); } catch (IOException e) {
	 * e.printStackTrace(); } return true; } };
	 * 
	 * addItemButton.setOnTouchListener(addListener);
	 */

	public void addXPTO(View view) {
		//new EndpointsTask().execute(getApplicationContext());
		String name = ((EditText) findViewById(R.id.activity_test_add_to_product_list_name)).getText().toString();
		Long barCode = Long.parseLong(((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode)).getText().toString());
		String description =  ((EditText) findViewById(R.id.activity_test_add_to_product_list_description)).getText().toString();
		new EndpointCall().AddProductTask(name, barCode, description);
	}

	/*FIXME LIXO public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
		@Override
		protected Long doInBackground(Context... contexts) {
			Productendpoint.Builder endpointBuilder = new Productendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});
			Productendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();
			try {
				String name = ((EditText) findViewById(R.id.activity_test_add_to_product_list_name)).getText().toString();
				Long barCode = Long.parseLong(((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode)).getText().toString());
				String description =  ((EditText) findViewById(R.id.activity_test_add_to_product_list_description)).getText().toString();

				//Product productAux = new Product(idProduct, name, barCode, description);
				
				epic.easystock.productendpoint.model.Product content = new epic.easystock.productendpoint.model.Product();
				
				content.setName(name);
				content.setBarCode(barCode);
				content.setDescription(description);
				
				epic.easystock.productendpoint.model.Product result = endpoint.insertProduct(content).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}*/
}
