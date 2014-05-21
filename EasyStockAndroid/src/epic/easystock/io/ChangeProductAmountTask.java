package epic.easystock.io;

import android.os.AsyncTask;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalProduct;
import epic.easystock.data.PantriesDBAdapter;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.data.ProductsDbAdapter;

public class ChangeProductAmountTask extends AsyncTask<LocalMetaProduct, Object, Object> {
	PantryDB dbAdapter;
	public ChangeProductAmountTask(PantryDB pantryDB) {
		dbAdapter = pantryDB;
	}

	@Override
	protected Object doInBackground(LocalMetaProduct... arg0) {
		dbAdapter.updateProduct(arg0[0]);
		
		
		return null;
	}

}
