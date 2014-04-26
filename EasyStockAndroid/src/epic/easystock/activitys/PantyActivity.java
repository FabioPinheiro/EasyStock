package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.assist.PantryDbAdapter;
import epic.easystock.data.LocalMetaProduct;

public class PantyActivity extends ListActivity {
	String mail;
	Button addProduct;
	private String name;
	MetaProductAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		mail = getIntent().getStringExtra("MAIL");
		addProduct = (Button) findViewById(R.id.AddProduct);
		adapter = new MetaProductAdapter(this,
				new ArrayList<LocalMetaProduct>());
		setListAdapter(adapter);
		selectPantry();

		addProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ADDPRODUCT PANTRY", "new AddProductTask: " + mail);
				new AddProductTask(adapter).execute(getApplicationContext());
			}
		});

	}

	private void selectPantry() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Title");
		alert.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				name = input.getText().toString();
				if (Strings.isNullOrEmpty(name))
					name = "default";
				if (isConnected()) {
					new ListPantryTask(adapter)
							.execute(getApplicationContext());
				} else {
					new LocalListPantryTask(adapter)
							.execute(getApplicationContext());
				}

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						name = "default";
					}
				});

		alert.show();
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
		private MetaProductAdapter adapter;

		public LocalListPantryTask(MetaProductAdapter adapter) {
			this.adapter = adapter;
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
			Log.i("PantyActivity", "new LocalListPantryTask: " + mail);
			if (!isSignedIn()) {
				return null;
			}
			return dbAdapter.getAllProducts();
		}
	}

	public class ListPantryTask extends
			AsyncTask<Context, Integer, List<LocalMetaProduct>> {
		private MetaProductAdapter adapter;
		private PantryDbAdapter dbAdapter;

		public ListPantryTask(MetaProductAdapter adapter) {
			this.adapter = adapter;
			dbAdapter = new PantryDbAdapter(PantyActivity.this);
			dbAdapter.open();
		}

		@Override
		protected void onPostExecute(List<LocalMetaProduct> result) { // FIXME
																		// passar
																		// o
																		// localProducts
																		// por
																		// return(mas
																		// não
																		// há
																		// problema)
			super.onPostExecute(result);
			dbAdapter.putAllProducts(result);
			Collection<LocalMetaProduct> localProducts = result;
			adapter.addAll(localProducts);
		}

		@Override
		protected List<LocalMetaProduct> doInBackground(Context... contexts) {
			Log.i("PantyActivity", "new ListPantryTask: " + mail);
			if (!isSignedIn()) {
				Log.e("PantyActivity", "!isSignedIn()=" + !isSignedIn()
						+ " email" + mail);
				return null; // FIXME falta informar o utilizador
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(PantyActivity.this)) {
				Log.e("PantyActivity",
						"fail to checkGooglePlayServicesAvailable" + " email"
								+ mail);
				return null; // FIXME informar o utilizador
			}

			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(PantyActivity.this, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);// FIXME

			List<MetaProduct> products = null;
			try {
				Pantry pantry = endpoint.getPantryByMailAndName(mail, name)
						.execute();

				products = pantry.getProducts();
				if (products == null) {
					Log.e("PantyActivity", "ListPantryTask PANTRY "
							+ "products == null");
					products = new ArrayList<MetaProduct>();
					pantry.setProducts(products);
					endpoint.updatePantry(pantry).execute();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<LocalMetaProduct> localProducts = new ArrayList<LocalMetaProduct>();
			for (MetaProduct mp : products) {
				Product aux = null;
				try {
					aux = endpoint.getProduct(mp.getProduct()).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				localProducts.add(new LocalMetaProduct(aux.getBarCode(), aux
						.getName(), aux.getDescription(), aux.getKey(), mp
						.getAmount()));
			}
			return localProducts;
		}
	}

	public class AddProductTask extends
			AsyncTask<Context, Integer, LocalMetaProduct> { // FIXME isto tem
															// muito problemas
															// de syscronização
															// (falar com o
															// fabio)
		MetaProductAdapter adapter;

		public AddProductTask(MetaProductAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		protected LocalMetaProduct doInBackground(Context... contexts) {
			Log.i("PantyActivity", "new ListPantryTask: " + mail);
			if (!isSignedIn()) {
				Log.e("PantyActivity", "!isSignedIn()=" + !isSignedIn()
						+ " email" + mail);
				return null; // FIXME falta informar o utilizador
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(PantyActivity.this)) {
				Log.e("PantyActivity",
						"fail to checkGooglePlayServicesAvailable" + " email"
								+ mail);
				return null; // FIXME informar o utilizador
			}

			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(PantyActivity.this, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);// FIXME

			Long productId = Long
					.valueOf(((EditText) findViewById(R.id.NumberId)).getText()
							.toString());
			Log.i("PantyActivity", "AddProductTask:" + "productId " + productId);
			try {
				Pantry pantry = endpoint.getPantryByMailAndName(mail, name).execute();
				List<MetaProduct> newList = pantry.getProducts();
				if (newList == null) {
					Log.e("PantyActivity", "AddProductTask: newList=null");
					newList = new ArrayList<MetaProduct>(); // FIXME into nuca
															// devia de ser null
				}
				MetaProduct metaP = new MetaProduct();
				Product newProd = endpoint.getProductByBarCode(productId)
						.execute();
				metaP.setProduct(newProd.getKey());
				metaP.setAmount(0.0);
				endpoint.insertMetaProduct(metaP).execute();
				newList.add(metaP);
				pantry.setProducts(newList);
				endpoint.updatePantry(pantry).execute();
				Log.i("PantyActivity", "AddProductTask:"
						+ "Product added to pantry");
				return new LocalMetaProduct(newProd.getBarCode(),
						newProd.getName(), newProd.getDescription(),
						newProd.getKey(), metaP.getAmount());
			} catch (IOException e) {
				Log.e("PantyActivity", "AddProductTask:"
						+ "Product NOT added to pantry");
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(LocalMetaProduct result) {
			super.onPostExecute(result);
			adapter.add(result);
			((EditText) findViewById(R.id.NumberId)).setText("");
		}

	}
}
