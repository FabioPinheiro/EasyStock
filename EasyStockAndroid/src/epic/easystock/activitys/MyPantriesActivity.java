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
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.assist.AppConstants;

public class MyPantriesActivity extends Activity {
	String mail;
	Button newPantry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pantries);

		mail = getIntent().getStringExtra("MAIL");

		TextView myMail = (TextView) findViewById(R.id.myMail);
		myMail.setText(mail);

		newPantry = (Button) findViewById(R.id.createpantry);

		newPantry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new NewPantryTask().execute(getApplicationContext());
			}
		});

	}

	private boolean isSignedIn() {
		if (!Strings.isNullOrEmpty(mail)) {
			return true;
		} else {
			return false;
		}
	}

	public class NewPantryTask extends AsyncTask<Context, Integer, Void> {
		@Override
		protected Void doInBackground(Context... contexts) {

			if (!isSignedIn()) {
				return null;
			}

			if (!AppConstants
					.checkGooglePlayServicesAvailable(MyPantriesActivity.this)) {

				return null;
			}

			// Create a Google credential since this is an authenticated request
			// to the API.
			GoogleAccountCredential credential = GoogleAccountCredential
					.usingAudience(MyPantriesActivity.this,
							AppConstants.AUDIENCE);
			credential.setSelectedAccountName(mail);
			ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);// FIXME

			try {

				Pantry myNewPantry;
				UserPantry newUP = new UserPantry();
				User user = new User();
				user.setEmail(mail);
				String[] uMail = mail.split("@");
				user.setNick(uMail[0]);
				myNewPantry = endpoint.getMyPantryByMail(mail).execute();
				if (myNewPantry == null) {
					myNewPantry = new Pantry();
					Log.i("NewPantryTask PANTRY", "myNewPantry == null"
							+ myNewPantry.getKey());
				}
				myNewPantry.setProducts(new ArrayList<MetaProduct>());

				try {
					user = endpoint.insertUser(user).execute();
				} catch (Exception e) {
					user = endpoint.findUserByMail(user).execute();
				}
				try {
					myNewPantry = endpoint.insertPantry(myNewPantry).execute();
				} catch (Exception e) {
					//myNewPantry = endpoint.updatePantry(myNewPantry).execute();
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
