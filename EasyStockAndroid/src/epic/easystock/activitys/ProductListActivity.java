package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import epic.easystock.R;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.assist.endPointCall.EndPointCall;
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;
import epic.easystock.apiEndpoint.model.Key;
import epic.easystock.apiEndpoint.model.Product;

public class ProductListActivity extends ListActivity {

	private View viewContainer;
	private View xpto;
	private String mail;
	//private ProductAUX product;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		xpto = findViewById(R.id.layout_edit_botons_product_list);
		// FIXME viewContainer = findViewById(R.id.undobar);

		mail = EndPointCall.getEmailAccount();//FIXME LIXO getIntent().getStringExtra("MAIL");
		// 1. pass context and data to the custom adapter
		ProductAdapter adapter = new ProductAdapter(this,
				new ArrayList<Product>());
		// 2. setListAdapter
		setListAdapter(adapter);
		new EndpointTask2(adapter).execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Product product = (Product) getListAdapter().getItem(position);
		//Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		xpto.setVisibility(View.VISIBLE);
		TextView x = (TextView) findViewById(R.id.myMail);
		x.setText(product.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showUndo(viewContainer);
		return true;
	}

	public void onClick(View view) {
		Toast.makeText(this, "Deletion undone", Toast.LENGTH_LONG).show();
		viewContainer.setVisibility(View.GONE);
	}

	public static void showUndo(final View viewContainer) {
		viewContainer.setVisibility(View.VISIBLE);
		viewContainer.setAlpha(1);
		viewContainer.animate().alpha(0.4f).setDuration(5000)
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						viewContainer.setVisibility(View.GONE);
					}
				});
	}

	private boolean isSignedIn() {
		if (!Strings.isNullOrEmpty(mail)) {
			return true;
		} else {
			return false;
		}
	}

	public class EndpointTask2 extends AsyncTask<Void, Void, List<Product>> {
		private ProductAdapter adapter;

		public EndpointTask2(ProductAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);
			Collection<Product> aux = result;
			adapter.addAll(aux);
		}

		@Override
		protected List<Product> doInBackground(Void... params) {
			CollectionResponseProduct result = null;
			if (!isSignedIn()) {
				return null;
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(ProductListActivity.this)) {
				return null;
			}

			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(ProductListActivity.this, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			try {
				result = AppConstants.getApiServiceHandle(credential)
						.listProduct().execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null) {
				return result.getItems();
			} else {
				return null;
			}
		}

	}

}
