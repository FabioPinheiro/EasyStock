package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.PantrySynchronizationDTO;
import epic.easystock.data.PantriesDBAdapter.PantryDB;

public class SynchronizePantryTask extends
AsyncTask<Void, Void, PantrySynchronizationDTO> {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private PantryDB pantryDB;
	private boolean fail_to_update = false;
	
	SynchronizePantryTask(PantryDB pantryDB) {
		this.pantryDB = pantryDB;
	}
	
	@Override
	protected void onPostExecute(PantrySynchronizationDTO result) {
		super.onPostExecute(result);
		if (fail_to_update) {
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_SYNCHRONIZED_PANTRY);
		} else {
			pantryDB.synch(result);
			EndPointCall.msg(LOG_TAG, EndPointCall.PANTRY_SYNCHRONIZED);
		}
	}
	
	@Override
	protected PantrySynchronizationDTO doInBackground(Void... vvv) {
		// List<LocalMetaProduct> list = pantryDB.getAllChangedProducts();
		PantrySynchronizationDTO dto = pantryDB.getPantrySynchronizationDTO();
		try {
			ApiEndpoint api = EndPointCall.getApiEndpoint();
			Log.i("PantrySynchronizationDTO", "SynchronizePantryTask: " + dto.getPantryKey() + " ");
			PantrySynchronizationDTO ret = api.synchronizationPantry(dto).execute();
			Log.d("PantrySynchronizationDTO", "SynchronizePantryTask: " + ret.getPantryKey() + " DONE");
			if(ret == null) throw new RuntimeException();
			//ERROR if(ret.getListMetaProducts() == null) throw new RuntimeException();
			if(ret.getListMetaProducts() == null){
				Log.e(LOG_TAG, "ret.getListMetaProducts() == null"); //FIXME
				ret.setListMetaProducts(new ArrayList<MetaProduct>());
			}
			return ret;
		} catch (IOException e) {
			fail_to_update = true;
			Toast.makeText(EndPointCall.getGlobalContext(),"SynchronizePantryTask: " + e.getMessage() + " " + dto.getPantryKey(), Toast.LENGTH_LONG).show();
			Log.e(LOG_TAG, "Fail to update " + dto.getPantryKey(), e); // FIXME TEXT
			return null;
		}
	}
}
