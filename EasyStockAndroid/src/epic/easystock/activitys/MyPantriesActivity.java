package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.ApiEndpoint.InsertUserPantry;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.apiEndpoint.model.UserPantryDTO;
import epic.easystock.assist.AppConstants;
import epic.easystock.io.AddProductToPantryTask;
import epic.easystock.io.EndPointCall;

public class MyPantriesActivity extends Activity {
	// String mail;
	Button newPantry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pantries);
		TextView myMail = (TextView) findViewById(R.id.myMail);
		myMail.setText(EndPointCall.getEmailAccount());

		//FIXME final String[] pantreisName = EndPointCall.getUserDbAdapter().avalablePantrysNamesFromUser(EndPointCall.getEmailAccount());
		
		newPantry = (Button) findViewById(R.id.createnewpantry);
		newPantry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				xpto();
			}
		});
	}
	public void xpto() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New Pantry in " + EndPointCall.getEmailAccount()); // TEXT
																			// FIXME
		alert.setMessage("Chose the Pantry Name:"); // TEXT FIXME
		final EditText textView = new EditText(this);
		alert.setView(textView);
		final Context aux2 = this;
		alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				new NewPantryTask(textView.getText().toString()).execute(aux2);
			}
		});
		alert.show();
	}
	
	private enum State {NULL, FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME , FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER,INSERT_NEW_USER_IN_APPENGINE}
	public class NewPantryTask extends AsyncTask<Context, Integer, UserPantry> {
		/* FIXME static */private final String LOG_TAG = AddProductToPantryTask.class.getCanonicalName();
		private String pantryName;
		private State state = State.NULL;
		
		NewPantryTask(String pantryName) {
			this.pantryName = pantryName;
		}
		
		@Override
		protected void onPostExecute(UserPantry result) {
			super.onPostExecute(result);
			if(result != null){
				EndPointCall.getUserDbAdapter().createPantry(EndPointCall.getEmailAccount(), result.getPantry(), pantryName);
				EndPointCall.msg(EndPointCall.DEBUG, "! "+ EndPointCall.getUserDbAdapter().getAllPantry().size());
			}
			EndPointCall.msg(LOG_TAG, EndPointCall.DONE);
			if(state == state.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME)
				EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME);
			if(state == state.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER)
				EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER);
			if(state == state.INSERT_NEW_USER_IN_APPENGINE)
				EndPointCall.msg(LOG_TAG, EndPointCall.INSERT_NEW_USER_IN_APPENGINE);
		}
		
		@Override
		protected UserPantry doInBackground(Context... v) {
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
					User user = EndPointCall.getUser();
					user = api1.findUserByMail(user).execute();
					if (user == null) {
						user = api1.insertUser(EndPointCall.getUser()).execute(); //FIXME isto devia esta noutro sitio
						Log.e(LOG_TAG, EndPointCall.INSERT_NEW_USER_IN_APPENGINE); //FIXME Log.i verificar só se funciona
						state=State.INSERT_NEW_USER_IN_APPENGINE;
					}
					UserPantryDTO aux = new UserPantryDTO();
					aux.setUser(user);
					aux.setPantry(null); //FIXME isto é suporto não fazer nada mas não tenho acertesa do defalte
					aux.setPantryName(pantryName);
					UserPantry xxx= api1.insertUserPantry(aux).execute();
					return xxx;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_pantries, menu);
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
}
