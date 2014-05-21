package epic.easystock.io;

import java.util.List;

import android.os.AsyncTask;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.assist.PantryAdapter;
import epic.easystock.data.UserBdAdapter.UserPantryAux;

public class ListAllPantriesTask extends AsyncTask<PantryAdapter, Void, Void>{
	List<UserPantryAux> pantries;
	public ListAllPantriesTask(List<UserPantryAux> availablePantriesFromUser) {
		this.pantries = availablePantriesFromUser;
	}

	@Override
	protected Void doInBackground(PantryAdapter... adapter) {
		adapter[0].addAll(pantries);
		
		return null;
	}
		

}
