package epic.easystock.io;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.apiEndpoint.model.UserPantryDTO;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.PantriesDBAdapter;

enum State {NULL, FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME , FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER,INSERT_NEW_USER_IN_APPENGINE}

public class NewPantryTask extends AsyncTask<Void, Integer, UserPantry> {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private String pantryName;
	private State state = State.NULL;
	
	NewPantryTask(String pantryName) {
		this.pantryName = pantryName;
	}
	
	@Override
	protected void onPostExecute(UserPantry result) {
		super.onPostExecute(result);
		if(result != null){
			EndPointCall.getUserDBAdapter().createPantry(EndPointCall.getEmailAccount(), result.getPantry(), pantryName);
			EndPointCall.setLastUsedPantry(result.getPantry());
			//PantriesDbAdapter.PantryDB aux = EndPointCall.getPantryDB(pantryName);//FIXME REMOVE
			EndPointCall.msg(EndPointCall.DEBUG, "! Pantry size= "+ EndPointCall.getUserDBAdapter().getAllPantry().size()); //FIXME REMOVE
			EndPointCall.msg(LOG_TAG, EndPointCall.DONE);
		}
		if(state == State.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME)
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME);
		if(state == State.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER)
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER);
		if(state == State.INSERT_NEW_USER_IN_APPENGINE)
			EndPointCall.msg(LOG_TAG, EndPointCall.INSERT_NEW_USER_IN_APPENGINE);
	}
	
	@Override
	protected UserPantry doInBackground(Void... vvv) {
		if (Strings.isNullOrEmpty(pantryName)) {
			Log.e(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME);
			state=State.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME;
		} else {
			try {
				ApiEndpoint api1 = EndPointCall.getApiEndpoint();
				/*FIXME ERROR in appengine Pantry myNewPantry = api1.getPantryByMailAndName(EndPointCall.getEmailAccount(), pantryName).execute();
				if (myNewPantry != null) {
					Log.e(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER + ": " + pantryName);
					state=State.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER;
					return null;// FIXME update
				}*/
				User user = api1.getUserByEmail(EndPointCall.getEmailAccount()).execute();
				if (user == null) {
					throw new RuntimeException(); //FIXME REMOVE
				}
				UserPantryDTO aux = new UserPantryDTO();
				aux.setUser(user);
				//Pantry p = new Pantry().setTimeStamp(new DateTime( new Date()));
				aux.setPantryTimeStamp(new DateTime( new Date()));
				aux.setPantry(null); //FIXME isto é suporto não fazer nada mas não tenho acertesa do defalte
				aux.setPantryName(pantryName);
				UserPantry xxx= api1.insertUserPantry(aux).execute();
				return xxx;
			} catch (IOException e) {
				new RuntimeException(); //FIXME REMOVE
				e.printStackTrace();
			}
		}
		return null;
	}
}
