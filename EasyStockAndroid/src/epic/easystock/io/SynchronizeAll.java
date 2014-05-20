package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.ApiEndpoint.GetPantry;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.PantrySynchronizationDTO;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.data.UserBdAdapter.UserPantryAux;

public class SynchronizeAll extends AsyncTask<Void, Void, List<Pantry>> {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private String email;
	private boolean fail_to_update = false;
	
	SynchronizeAll(String email) {
		this.email = email;
	}
	
	@Override
	protected void onPostExecute(List<Pantry> pantriesToSync) {
		super.onPostExecute(pantriesToSync);

		if(fail_to_update){
			EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_SYNCHRONIZED_USER);
		}else{
			for (Pantry pantry : pantriesToSync) {
				PantryDB pantryDB = EndPointCall.getPantryDB(pantry.getKey());
				new SynchronizePantryTask(pantryDB).execute();
			}
			EndPointCall.msg(LOG_TAG, EndPointCall.USER_SYNCHRONIZED);
		}
	}
	
	@Override
	protected List<Pantry> doInBackground(Void... vvv) {
		try {
			ApiEndpoint api =  EndPointCall.getApiEndpoint();
			User user = api.getUserByEmail(EndPointCall.getEmailAccount()).execute();
			if (user == null) {
				user = api.insertUser(email).execute();
				Log.e(LOG_TAG, EndPointCall.INSERT_NEW_USER_IN_APPENGINE);
				//state=State.INSERT_NEW_USER_IN_APPENGINE; //FIXME TODO
			}
			List<UserPantry> remoteUserPantries = user.getUserPantriesList();  // ERROR is null !!! 
			//FIXME List<UserPantry> remoteUserPantries = api.listUserPantryOfUser(email).execute().getItems();
			List<UserPantryAux> all = EndPointCall.getUserDBAdapter().getAllPantry();
			List<UserPantry> userPantryToSync = new ArrayList<UserPantry>();
			List<UserPantry> userPantryToCreate = new ArrayList<UserPantry>();
			for (UserPantryAux userPantryAux : all) {
				if(userPantryAux.user == email){
					for (UserPantry remoteUserPantry : remoteUserPantries) {
						if(remoteUserPantry.getKey().getId() == userPantryAux.pantryID){
							//TODO if(date)
							userPantryToSync.add(remoteUserPantry);
						}else{ //new pantry to the 
							//NONE
						}
					}
				}else{
					//NONE
				}
			}
			for (UserPantry remoteUserPantry : remoteUserPantries) { 
				if(!userPantryToSync.contains(remoteUserPantry)){
					userPantryToCreate.add(remoteUserPantry);
				}
			}
			//
			List<Pantry> pantriesToSync = new ArrayList<Pantry>();
			for (UserPantry  aux : userPantryToSync) { 
				pantriesToSync.add(EndPointCall.getApiEndpoint().getPantry(aux.getPantry()).execute());
			}
			for (UserPantry  aux : userPantryToCreate) { 
				Pantry iii = EndPointCall.getApiEndpoint().getPantry(aux.getPantry()).execute();
				pantriesToSync.add(iii);
				EndPointCall.getUserDBAdapter().createPantry(email, iii.getKey(), iii.getName());
			}
			return pantriesToSync;
		} catch (IOException e) {
			fail_to_update = true;
			Log.e(LOG_TAG, "Fail to update", e); //FIXME TEXT
		}
		
		return null;
	}
}
