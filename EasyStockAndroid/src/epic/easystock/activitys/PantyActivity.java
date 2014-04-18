package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.assist.AppConstants;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.MetaProduct;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.Product;

public class PantyActivity extends ListActivity {
	String mail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		mail = getIntent().getStringExtra("MAIL");
		new ListPantryTask().execute(getApplicationContext());
	}

	private boolean isSignedIn() {
		if (!Strings.isNullOrEmpty(mail)) {
			return true;
		} else {
			return false;
		}

	}

	public class ListPantryTask extends AsyncTask<Context, Integer, Void> {
		@Override
		protected Void doInBackground(Context... contexts) {

			if (!isSignedIn()) {
				return null;
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(PantyActivity.this)) {
				return null;
			}

			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(PantyActivity.this, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);// FIXME

			List<MetaProduct> products = null;
			try {
				products = endpoint.getPantryProducts(12l).execute().getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<Product> list = new ArrayList<Product>(
					products.size());
			for (MetaProduct mp : products) {
				Product aux = new Product();
				aux.setName(mp.getProduct().getName());
				aux.setDescription(mp.getProduct().getDescription());
				aux.setBarCode(mp.getProduct().getBarCode());
				aux.setKey(mp.getProduct().getKey());
				list.add(aux);
			}
			// FIXME ProductAdapter adapter = new ProductAdapter(contexts[0],
			// list);

			// 2. setListAdapter
			// FIXME setListAdapter(adapter);

			return null;
		}
	}
}
