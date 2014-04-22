package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.AppConstants;

public class PantyActivity extends ListActivity {
	String mail;
	Button addProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		
		addProduct = (Button) findViewById(R.id.AddProduct);
		
		addProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AddProductTask().execute(getApplicationContext());
			}
		});

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
			Long pantryId = 0l;
			try {
				Pantry pantry = endpoint.getPantryIdByMail(mail).execute();
				products = endpoint.getPantryProducts(pantry.getKey().getId())
						.execute().getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<Product> list = new ArrayList<Product>(products.size());
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

	public class AddProductTask extends AsyncTask<Context, Integer, Void> {
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

			Long pantryId = Long
					.valueOf(((EditText) findViewById(R.id.NumberId)).getText()
							.toString());
			try {
				Pantry pantry = endpoint.getPantryIdByMail(mail).execute();

				List<MetaProduct> newList = pantry.getProducts();
				newList.add(new MetaProduct().setProduct(endpoint.getProduct(
						pantryId).execute()));
				pantry.setProducts(newList);
				endpoint.updatePantry(pantry);

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}
}
