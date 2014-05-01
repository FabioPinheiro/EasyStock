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
import epic.easystock.apiEndpoint.model.CollectionResponseProduct;
import epic.easystock.apiEndpoint.model.Key;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.io.EndPointCall;

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
		ProductAdapter adapter = new ProductAdapter(this, new ArrayList<Product>());
		// 2. setListAdapter
		setListAdapter(adapter);
		EndPointCall.listProductTask(adapter);
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
}
