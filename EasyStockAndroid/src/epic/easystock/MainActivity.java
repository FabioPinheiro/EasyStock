package epic.easystock;

import android.app.ListActivity;
import android.os.Bundle;

public class MainActivity extends ListActivity {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// 1. pass context and data to the custom adapter
		//ProductAdapter adapter = new ProductAdapter(this, generateData());

		// 2. setListAdapter
		//setListAdapter(adapter);
	}
	
	/*LIXO
	private ArrayList<ProductAUX> generateData() {
		ArrayList<ProductAUX> productsList = new ArrayList<ProductAUX>();
		productsList.add(new ProductAUX(1l, "name1", 11l, "description1"));
		productsList.add(new ProductAUX(2l, "name2", 12l, "description2"));
		productsList.add(new ProductAUX(3l, "name3", 13l, "description3"));
		productsList.add(new ProductAUX(4l, "name4", 14l, "description4"));
		productsList.add(new ProductAUX(5l, "name5", 15l, "description5"));
		productsList.add(new ProductAUX(6l, "name6", 16l, "description6"));
		productsList.add(new ProductAUX(7l, "name7", 17l, "description7"));
		productsList.add(new ProductAUX(8l, "name8", 18l, "description8"));

		return productsList;
	}*/

}
