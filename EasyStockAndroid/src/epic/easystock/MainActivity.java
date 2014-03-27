package epic.easystock;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

public class MainActivity extends ListActivity {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// 1. pass context and data to the custom adapter
		ProductAdapter adapter = new ProductAdapter(this, generateData());

		// 2. setListAdapter
		setListAdapter(adapter);
	}

	private ArrayList<Product> generateData() {
		ArrayList<Product> productsList = new ArrayList<Product>();
		productsList.add(new Product(1l, "name1", "barCode1", "description1"));
		productsList.add(new Product(2l, "name2", "barCode2", "description2"));
		productsList.add(new Product(3l, "name3", "barCode3", "description3"));
		productsList.add(new Product(4l, "name4", "barCode4", "description4"));
		productsList.add(new Product(5l, "name5", "barCode5", "description5"));
		productsList.add(new Product(6l, "name6", "barCode6", "description6"));
		productsList.add(new Product(7l, "name7", "barCode7", "description7"));
		productsList.add(new Product(8l, "name8", "barCode8", "description8"));

		return productsList;
	}

}
