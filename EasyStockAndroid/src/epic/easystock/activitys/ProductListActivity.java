package epic.easystock.activitys;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import epic.easystock.R;
import epic.easystock.assist.EndpointCall;
import epic.easystock.assist.ProductAUX;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.metaproductendpoint.model.Key;
import epic.easystock.metaproductendpoint.model.Product;

public class ProductListActivity extends ListActivity {
	
	private View viewContainer;
	private View xpto;
	private ProductAUX product;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		
		xpto = findViewById(R.id.layout_edit_botons_product_list);
		//FIXME viewContainer = findViewById(R.id.undobar);
		
		// 1. pass context and data to the custom adapter
		ProductAdapter adapter = new ProductAdapter(this, generateData());
		// 2. setListAdapter
		setListAdapter(adapter);
		EndpointCall.listProductTask(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		product = (ProductAUX) getListAdapter().getItem(position);
		//Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		xpto.setVisibility(View.VISIBLE);
		TextView x = (TextView) findViewById(R.id.textView1);
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
	
	
	private ArrayList<epic.easystock.productendpoint.model.Product> generateData() { //FIXME remove me
		ArrayList<epic.easystock.productendpoint.model.Product> productsList = new ArrayList<epic.easystock.productendpoint.model.Product>();
		for(int i=10; i<20; i++){
			Product aux = new Product();
			aux.setBarCode((long) i+ 9000);
			aux.setDescription("description " + i + " !");
			aux.setKey(new Key());
			aux.setName("name " + i + " !");
		}
		return productsList;
	}
}
