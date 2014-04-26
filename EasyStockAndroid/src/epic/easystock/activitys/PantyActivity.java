package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.internal.en;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
//github.com/FabioPinheiro/EasyStock.git
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.PantryDbAdapter;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.data.LocalMetaProduct;

public class PantyActivity extends ListActivity {
	String mail;
	Button addProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		mail = getIntent().getStringExtra("MAIL");
		addProduct = (Button) findViewById(R.id.AddProduct);

		if (false/*isConnected()*/) {
			ProductAdapter adapter = new ProductAdapter(this,
					new ArrayList<Product>());
			setListAdapter(adapter);
			new ListPantryTask(adapter).execute(getApplicationContext());
		} else {
			ArrayAdapter<LocalMetaProduct> adapter = new ArrayAdapter<LocalMetaProduct>(
					this, android.R.layout.simple_selectable_list_item);
			setListAdapter(adapter);
			new LocalListPantryTask().execute(getApplicationContext());
		}

		addProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ADDPRODUCT PANTRY", "new AddProductTask: " + mail);
				new AddProductTask().execute(getApplicationContext());
			}
		});
	}

	private boolean isConnected() {
		boolean connected = false;
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ? null
				: conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
						.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null ? null
				: conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.getState();
		if (mobile != null
				&& (mobile == State.CONNECTED || mobile == State.CONNECTING)) {
			connected = true;
		}
		if (wifi != null
				&& (wifi == State.CONNECTED || wifi == State.CONNECTING)) {
			connected = true;
		}
		return connected;

	}

	private boolean isSignedIn() {
		if (!Strings.isNullOrEmpty(mail)) {
			return true;
		} else {
			return false;
		}
	}

	public class LocalListPantryTask extends
			AsyncTask<Context, Integer, List<LocalMetaProduct>> {
		private PantryDbAdapter dbAdapter;

		public LocalListPantryTask() {
			dbAdapter = new PantryDbAdapter(PantyActivity.this);
			dbAdapter.open();
		}

		@Override
		protected void onPostExecute(List<LocalMetaProduct> result) {
			super.onPostExecute(result);
			@SuppressWarnings("unchecked")
			ArrayAdapter<LocalMetaProduct> adapter = (ArrayAdapter<LocalMetaProduct>) getListAdapter();
			adapter.addAll(result);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected List<LocalMetaProduct> doInBackground(Context... contexts) {
			Log.i("LocalListPantryTask PANTRY", "new LocalListPantryTask: "
					+ mail);
			if (!isSignedIn()) {
				return null;
			}
			return dbAdapter.getAllProducts();
		}
	}

	public class ListPantryTask extends
			AsyncTask<Context, Integer, List<Product>> {
		private ProductAdapter adapter;
		private PantryDbAdapter dbAdapter;
		private List<LocalMetaProduct> localProducts;
		public ListPantryTask(ProductAdapter adapter) {
			this.adapter = adapter;
			dbAdapter = new PantryDbAdapter(PantyActivity.this);
			dbAdapter.open();
			localProducts = new ArrayList<LocalMetaProduct>();
		}

		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);
			Collection<Product> aux = result;
			adapter.addAll(aux);
			dbAdapter.putAllProducts(localProducts);
		}

		@Override
		protected List<Product> doInBackground(Context... contexts) {
			Log.i("ListPantryTask PANTRY", "new ListPantryTask: " + mail);
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
				Pantry pantry = endpoint.getMyPantryByMail(mail).execute();

				products = pantry.getProducts();
				if (products == null) {
					Log.i("ListPantryTask PANTRY", "products == null");
					products = new ArrayList<MetaProduct>();
					pantry.setProducts(products);
					endpoint.updatePantry(pantry).execute();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			ArrayList<Product> list = new ArrayList<Product>();
			for (MetaProduct mp : products) {
				Product aux = null;
				try {
					aux = endpoint.getProduct(mp.getProduct()).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				localProducts.add(new LocalMetaProduct(aux.getBarCode(), aux.getName(), aux.getDescription(), aux.getKey(),mp.getAmount()));
				list.add(aux);
			}
			return list;
		}
	}

	
	public class AddProductTask extends AsyncTask<Context, Integer, Void> {
		@Override
		protected Void doInBackground(Context... contexts) {
			Log.i("ADDPRODUCT PANTRY", "doInBackground" + mail);
			if (!isSignedIn()) {
				return null;
			}
			Log.i("ADDPRODUCT PANTRY", "isSignedIn");
			if (!AppConstants
					.checkGooglePlayServicesAvailable(PantyActivity.this)) {

				return null;
			}
			Log.i("ADDPRODUCT PANTRY", " GooglePlayServices isAvailable");
			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(PantyActivity.this, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);// FIXME

			Long productId = Long
					.valueOf(((EditText) findViewById(R.id.NumberId)).getText()
							.toString());
			Log.i("ADDPRODUCT PANTRY", "productId " + productId);
			try {
				Pantry pantry = endpoint.getMyPantryByMail(mail).execute();
				List<MetaProduct> newList = pantry.getProducts();
				if (newList == null)
					newList = new ArrayList<MetaProduct>();
				MetaProduct metaP = new MetaProduct();
				Product newProd = endpoint.getProductByBarCode(productId)
						.execute();
				metaP.setProduct(newProd.getKey());
				metaP.setAmount(0.0);
				endpoint.insertMetaProduct(metaP).execute();
				newList.add(metaP);
				pantry.setProducts(newList);
				endpoint.updatePantry(pantry).execute();

				Log.i("ADDPRODUCT PANTRY", "Product added to pantry");
			} catch (IOException e) {
				Log.i("ADDPRODUCT PANTRY", "Product NOT added to pantry");
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProductAdapter adapter = new ProductAdapter(PantyActivity.this,
					new ArrayList<Product>());
			setListAdapter(adapter);
			new ListPantryTask(adapter).execute(PantyActivity.this);
		}

	}
}
