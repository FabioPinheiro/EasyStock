package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
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
			if(pantriesToSync.isEmpty()){
				EndPointCall.msg(LOG_TAG, EndPointCall.USER_WERE_ALREADY_SYNCHRONIZED);
			}else{
				for (Pantry pantry : pantriesToSync) {
					Log.d(LOG_TAG, "I will try to Synchronize Pantry with the key=" + pantry.getKey() );
					PantryDB pantryDB = EndPointCall.getPantryDB(pantry.getKey());
					new SynchronizePantryTask(pantryDB).execute();
				}
				EndPointCall.msg(LOG_TAG, EndPointCall.USER_SYNCHRONIZED);
			}
		}
	}
	
	@Override
	protected List<Pantry> doInBackground(Void... vvv) {
		try {
			ApiEndpoint api =  EndPointCall.getApiEndpoint();
			User user = api.getUserByEmail(EndPointCall.getEmailAccount()).execute();
			if (user == null) { //FIXME isto devia estar noutro sitio
				user = api.registerUser(email).execute();
				Log.e(LOG_TAG, EndPointCall.INSERT_NEW_USER_IN_APPENGINE);
				//state=State.INSERT_NEW_USER_IN_APPENGINE; //FIXME TODO
			}

			if(user.getUserPantriesList() == null){ //FIXME
				user.setUserPantriesList(new ArrayList<UserPantry>()); //ERROR REMOVE Por corrigir
				Log.e(LOG_TAG, "(user.getUserPantriesList() == null");
			}
			List<UserPantry> remoteUserPantries = user.getUserPantriesList();  // ERROR is null !!! 
			Log.d(LOG_TAG, "I already have all remoteUserPantries size=" + remoteUserPantries.size());
			
			//FIXME List<UserPantry> remoteUserPantries = api.listUserPantryOfUser(email).execute().getItems();
			List<UserPantryAux> all = EndPointCall.getUserDBAdapter().getAllPantry(); //ERROR só pode ser de um user
			List<UserPantry> userPantryToSync = new ArrayList<UserPantry>();
			List<UserPantry> userPantryToCreate = new ArrayList<UserPantry>();
			for (UserPantryAux userPantryAux : all) {
				if(userPantryAux.user == email){
					for (UserPantry remoteUserPantry : remoteUserPantries) {
						if(remoteUserPantry.getKey().getId() == userPantryAux.pantryID){
							Log.d(LOG_TAG, "I already known this remoteUserPantry key=" + remoteUserPantry.getKey().getId());
							//TODO Date a = new Date(remoteUserPantry.getPantry().getTimeStamp().getValue());
							//TODO Date b = new Date(userPantryAux.getPantry().getTimeStamp().getValue());
							//TODO if(remoteUserPantry.getPantry().getTimeStamp().after());
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
					Log.d(LOG_TAG, "I unknown this Pantry key=" + remoteUserPantry.getKey().getId());
					userPantryToCreate.add(remoteUserPantry);
				}
			}
			//
			List<Pantry> pantriesToSync = new ArrayList<Pantry>();
			for (UserPantry  aux : userPantryToSync) { 
				pantriesToSync.add(EndPointCall.getApiEndpoint().getPantry(aux.getPantry().getKey()).execute());
			}
			for (UserPantry  aux : userPantryToCreate) {
				Log.d(LOG_TAG, "I will try to create Pantry with the key=" + aux.getKey().getId() );
				Pantry iii = EndPointCall.getApiEndpoint().getPantry(aux.getPantry().getKey()).execute(); //FIXME ....
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
