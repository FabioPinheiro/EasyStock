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
import epic.easystock.Product;
import epic.easystock.ProductAdapter;
import epic.easystock.R;

public class ProductListActivity extends ListActivity {
	
	private View viewContainer;
	private View xpto;
	private Product product;
	
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
		
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		product = (Product) getListAdapter().getItem(position);
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
	
	
	private ArrayList<Product> generateData() { //FIXME
		ArrayList<Product> productsList = new ArrayList<Product>();
		productsList.add(new Product(1l, "name1", 11l, "description1"));
		productsList.add(new Product(2l, "name2", 12l, "description2"));
		productsList.add(new Product(3l, "name3", 13l, "description3"));
		productsList.add(new Product(4l, "name4", 14l, "description4"));
		productsList.add(new Product(5l, "name5", 15l, "description5"));
		productsList.add(new Product(6l, "name6", 16l, "description6"));
		productsList.add(new Product(7l, "name7", 17l, "description7"));
		productsList.add(new Product(8l, "name8", 18l, "description8"));

		return productsList;
	}
}
