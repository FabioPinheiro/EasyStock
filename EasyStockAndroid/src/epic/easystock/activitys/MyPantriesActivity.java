package epic.easystock.activitys;

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
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
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
		List<String> aux = EndPointCall.getUserDbAdapter().avalablePantrysFromUser(EndPointCall.getEmailAccount());
		final String[] pantry = new String[aux.size()];
		aux.toArray(pantry);
		alert.setTitle("New Pantry in " + EndPointCall.getEmailAccount()); // TEXT
																			// FIXME
		alert.setMessage("Chose the Pantry Name:"); // TEXT FIXME
		final TextView textView = new TextView(this);
		alert.setView(textView);
		alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				new NewPantryTask(textView.getText().toString()).execute(getApplicationContext());
			}
		});
		alert.show();
	}
	
	public class NewPantryTask extends AsyncTask<Context, Integer, Void> {
		/* FIXME static */private final String LOG_TAG = AddProductToPantryTask.class.getCanonicalName();
		private String pantryName;
		
		NewPantryTask(String pantryName) {
			this.pantryName = pantryName;
		}
		@Override
		protected Void doInBackground(Context... contexts) {
			if (Strings.isNullOrEmpty(pantryName)) {
				EndPointCall.msg(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME);
				Log.e(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME);
			} else {
				try {
					Pantry myNewPantry = EndPointCall.getApiEndpoint().getPantryByMailAndName(EndPointCall.getEmailAccount(), pantryName).execute();
					if (myNewPantry != null) {
						Log.e(LOG_TAG, EndPointCall.FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER + ": " + pantryName);
						return null;// FIXME update
					}
					User user = EndPointCall.getUser();
					user = EndPointCall.getApiEndpoint().findUserByMail(user).execute();
					if (user == null) {
						EndPointCall.getApiEndpoint().insertUser(EndPointCall.getUser()).execute();
					}
					myNewPantry = new Pantry();
					myNewPantry.setName(pantryName);
					myNewPantry.setProducts(new ArrayList<MetaProduct>()); // FIXME isto devia/ja estar no AppEngine
					myNewPantry = EndPointCall.getApiEndpoint().insertPantry(myNewPantry).execute();
					UserPantry newUP = new UserPantry();
					try {
						newUP.setUser(user.getKey().getId());
						newUP.setPantry(myNewPantry.getKey());
						endpoint.insertUserPantry(newUP).execute();
					} catch (Exception e) {
						endpoint.updateUserPantry(newUP).execute();
					}
				} catch (Exception e) {
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
