package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import epic.easystock.CloudEndpointUtils;
import epic.easystock.R;
import epic.easystock.assist.EndpointCall;
import epic.easystock.assist.ProductAUX;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;

public class PantyActivity extends ListActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_panty);
		new ListPantryTask().execute(getApplicationContext());


	}

	public class ListPantryTask extends AsyncTask<Context, Integer, Void> {
		@Override
		protected Void doInBackground(Context... contexts) {

			ApiEndpoint endpoint = EndpointCall.getApiEndpoint();//FIXME
			
			List<MetaProduct> products = null;
			try {
				 products = endpoint.getPantryProducts(12l)
						.execute().getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<ProductAUX> list = new ArrayList<ProductAUX>(products.size());
			for(MetaProduct mp:products){
				ProductAUX aux = new ProductAUX();
				aux.setQuantity(mp.getAmount().intValue());
				aux.setName(mp.getProduct().getName());
				aux.setDescription(mp.getProduct().getDescription());
				aux.setBarCode(mp.getProduct().getBarCode());
				aux.setIdProduct(mp.getProduct().getKey().getId());
				list.add(aux);
			}
			//FIXME ProductAdapter adapter = new ProductAdapter(contexts[0], list);

			// 2. setListAdapter
			//FIXME setListAdapter(adapter);

			return null;
		}
	}
}
