package epic.easystock.io;

import java.io.IOException;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.PantriesDbAdapter.PantryDB;

public class SynchronizePantryTask extends AsyncTask<Void, Void, Void> {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private PantryDB pantryDB;
	
	SynchronizePantryTask(PantryDB pantryDB) {
		this.pantryDB = pantryDB;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		List<LocalMetaProduct> list = pantryDB.getAllChangedProducts();
	}
	
	@Override
	protected Void doInBackground(Void... vvv) {
		return null;
	}
}
