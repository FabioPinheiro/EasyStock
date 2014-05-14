package epic.easystock.io;

import java.io.IOException;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.PantrySynchronizationDTO;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.data.UserBdAdapter.UserPantryAux;

public class SynchronizeUserDBTask extends AsyncTask<Void, Void, Void> {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private User user;
	private boolean fail_to_update = false;
	
	SynchronizeUserDBTask(User user) {
		this.user = user;
	}
	
	@Override
	protected void onPostExecute(Void vvv) {
		super.onPostExecute(vvv);
		List<UserPantryAux> aux = EndPointCall.getUserDBAdapter().avalablePantrysFromUser(user.getEmail());
		for (UserPantryAux userPantryAux : aux) {
			PantryDB pantryDB = EndPointCall.getPantryDB(userPantryAux.pantryID);
			//SynchronizePantry(pantryDB); 
			new SynchronizePantryTask(pantryDB).execute();//FIXME isto esta a quiar multiplas  use .get()
		}
		if(fail_to_update){
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_SYNCHRONIZED_USER);
		}else{
			pantryDB.update(result);
			EndPointCall.msg(LOG_TAG, EndPointCall.USER_SYNCHRONIZED);
		}
	}
	
	@Override
	protected User doInBackground(Void... vvv) {
		//List<LocalMetaProduct> list = pantryDB.getAllChangedProducts();
		PantrySynchronizationDTO dto = pantryDB.getPantrySynchronizationDTO();
		try {
			ApiEndpoint api = EndPointCall.getApiEndpoint();
			return api.updatePantry(dto).execute();
		} catch (IOException e) {
			fail_to_update = true;
			Log.e(LOG_TAG, "Fail to update", e); //FIXME TEXT
		}
		
		return null;
	}
}
