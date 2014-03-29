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
import epic.easystock.assist.ProductAUX;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.pantryendpoint.Pantryendpoint;
import epic.easystock.pantryendpoint.model.MetaProduct;

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
			Pantryendpoint.Builder endpointBuilder = new Pantryendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});
			Pantryendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();
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
