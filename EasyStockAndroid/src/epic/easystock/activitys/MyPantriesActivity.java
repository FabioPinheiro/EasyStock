package epic.easystock.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import epic.easystock.io.EndPointCall;

public class MyPantriesActivity extends Activity {
	//String mail;
	Button newPantry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pantries);

		//LIXO mail = getIntent().getStringExtra("MAIL");

		TextView myMail = (TextView) findViewById(R.id.myMail);
		myMail.setText(EndPointCall.getEmailAccount());

		newPantry = (Button) findViewById(R.id.createpantry);

		newPantry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new NewPantryTask().execute(getApplicationContext());
			}
		});

	}

	public class NewPantryTask extends AsyncTask<Context, Integer, Void> {
		private String name;

		@Override
		protected Void doInBackground(Context... contexts) {

			if (!EndPointCall.isSignedIn()) {
				return null;
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(MyPantriesActivity.this)) {

				return null;
			}
			name = ((EditText) findViewById(R.id.pantryName)).getText()
					.toString();
			if (Strings.isNullOrEmpty(name)) {
				name = "default";
			}
			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(MyPantriesActivity.this,
							AppConstants.AUDIENCE);
			credential.setSelectedAccountName(EndPointCall.getEmailAccount());
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);

			try {
					
				Pantry myNewPantry = endpoint
						.getPantryByMailAndName(EndPointCall.getEmailAccount(), name).execute();
				if (myNewPantry != null) {
					Log.e("CREATE PANTRY", "User have a pantry with "+ name+" as name");
					return null;
				}
				UserPantry newUP = new UserPantry();
				User user = new User();
				user.setEmail(EndPointCall.getEmailAccount());
				String[] uMail = EndPointCall.getEmailAccount().split("@");
				user.setNick(uMail[0]);
				myNewPantry = new Pantry();
				myNewPantry.setName(name);
				myNewPantry.setProducts(new ArrayList<MetaProduct>());

				try {
					user = endpoint.insertUser(user).execute();
				} catch (Exception e) {
					user = endpoint.findUserByMail(user).execute();
				}
				try {
					myNewPantry = endpoint.insertPantry(myNewPantry).execute();
				} catch (Exception e) {
					// myNewPantry =
					// endpoint.updatePantry(myNewPantry).execute();
					e.printStackTrace();
				}
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
