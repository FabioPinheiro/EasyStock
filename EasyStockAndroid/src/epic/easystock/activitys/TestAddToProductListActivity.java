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
import epic.easystock.R.id;
import epic.easystock.R.layout;
import epic.easystock.itemendpoint.Itemendpoint;
import epic.easystock.itemendpoint.model.ItemData;


public class TestAddToProductListActivity extends Activity {

	private OnTouchListener addListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
	}
		/*
		Button addItemButton = (Button) findViewById(R.id.button1);

		addListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
					String name = ((EditText) findViewById(R.id.itemName))
							.getText().toString();
					item.setName(name);

					String description = ((EditText) findViewById(R.id.descriptionText))
							.getText().toString();
					;
					item.setDescription(description);
					ItemData result = endpoint.insertItem(item).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		};
		
		addItemButton.setOnTouchListener(addListener);*/
	
	public void addXPTO(View view) {
		new EndpointsTask().execute(getApplicationContext());
	}
	
	public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
		@Override
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
				String name = ((EditText) findViewById(R.id.itemName))
						.getText().toString();
				item.setName(name);

				String description = ((EditText) findViewById(R.id.descriptionText))
						.getText().toString();
				;
				item.setDescription(description);
				ItemData result = endpoint.insertItem(item).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}
}
