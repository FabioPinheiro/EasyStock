package epic.easystock.activitys;

import java.io.IOException;
import java.util.List;

import epic.easystock.R;
import epic.easystock.R.id;
import epic.easystock.R.layout;
import epic.easystock.R.menu;
import epic.easystock.apiEndpoint.ApiEndpoint.ListUserPantryOfUser;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.apiEndpoint.model.UserPantryCollection;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalObject;
import epic.easystock.data.UserBdAdapter.UserPantryAux;
import epic.easystock.io.EndPointCall;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;



public class PantrySettingActivity extends Activity {

	private UserPantry userPantry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantry_setting);
		
		List<UserPantry> userPantries;
		String pantryName = EndPointCall.getmCurrentPantry();
		Long pantryId = 0l;
		List<UserPantryAux> pantries = EndPointCall.getUserDBAdapter().avalablePantrysFromUser(EndPointCall.getEmailAccount());
		for(UserPantryAux p : pantries){
			if(p.pantryName.equals(pantryName)){
				pantryId = p.pantryID;
				break;
			}
		}
		
		/*EndPointCall.getUserPantryTask(pantryId);
		try {
			userPantries = EndPointCall.getApiEndpoint().listUserPantryOfUser(EndPointCall.getEmailAccount()).execute().getItems();
			for(UserPantry p : userPantries){
				if(p.getPantry().getName().equals(pantryName)){
					userPantry = p;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		
		TextView edt = (TextView) findViewById(R.id.pantry_name);
		edt.setText(pantryName);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pantry_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_pantry_setting,
					container, false);
			return rootView;
		}
	}

}
