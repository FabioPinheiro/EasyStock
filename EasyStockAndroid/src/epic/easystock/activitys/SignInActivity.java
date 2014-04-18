package epic.easystock.activitys;

import java.io.IOException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import epic.easystock.R;
import epic.easystock.assist.*;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

public class SignInActivity extends Activity {
	private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;

	public static final String LOG_TAG = "SignInActivity:";

	private AuthorizationCheckTask mAuthTask;
	private String mEmailAccount = "";
	private com.google.android.gms.common.SignInButton button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		button = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onClickSignIn(v);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		button.callOnClick();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("MAIL", mEmailAccount);
		startActivity(intent);

	}

	public void onClickSignIn(View view) {
		TextView emailAddressTV = (TextView) view.getRootView().findViewById(
				R.id.email_address_tv);
		Toast.makeText(this, "onClickSignIn", Toast.LENGTH_LONG).show();
		// Check to see how many Google accounts are registered with the device.
		int googleAccounts = AppConstants.countGoogleAccounts(this);
		if (googleAccounts == 0) {
			// No accounts registered, nothing to do.
			Toast.makeText(this, "no_google_accounts_registered",
					Toast.LENGTH_LONG).show();
		} else if (googleAccounts == 1) {
			// If only one account then select it.
			AccountManager am = AccountManager.get(this);
			Account[] accounts = am
					.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			if (accounts != null && accounts.length > 0) {
				// Select account and perform authorization check.
				emailAddressTV.setText(accounts[0].name);
				mEmailAccount = accounts[0].name;
				performAuthCheck(accounts[0].name);
			}
		} else {
			// More than one Google Account is present, a chooser is necessary.

			// Reset selected account.
			emailAddressTV.setText("");

			// Invoke an {@code Intent} to allow the user to select a Google
			// account.
			Intent accountSelector = AccountPicker.newChooseAccountIntent(null,
					null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE },
					false, "Select the account to access the EasyStock API.",
					null, null, null);
			startActivityForResult(accountSelector,
					ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
		}
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("MAIL", mEmailAccount);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION
				&& resultCode == RESULT_OK) {
			// This path indicates the account selection activity resulted in
			// the user selecting a
			// Google account and clicking OK.

			// Set the selected account.
			String accountName = data
					.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			TextView emailAccountTextView = (TextView) this
					.findViewById(R.id.email_address_tv);
			emailAccountTextView.setText(accountName);

			// Fire off the authorization check for this account and OAuth2
			// scopes.
			performAuthCheck(accountName);
		}
	}

	public void performAuthCheck(String emailAccount) {
		// Cancel previously running tasks.
		if (mAuthTask != null) {
			try {
				mAuthTask.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		new AuthorizationCheckTask().execute(emailAccount);
	}

	class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... emailAccounts) {
			Log.i(LOG_TAG, "Background task started.");

			if (!AppConstants
					.checkGooglePlayServicesAvailable(SignInActivity.this)) {
				return false;
			}

			String emailAccount = emailAccounts[0];
			// Ensure only one task is running at a time.
			mAuthTask = this;

			// Ensure an email was selected.
			if (Strings.isNullOrEmpty(emailAccount)) {
				// publishProgress(R.string.toast_no_google_account_selected);
				// Failure.
				return false;
			}

			Log.d(LOG_TAG, "Attempting to get AuthToken for account: "
					+ mEmailAccount);

			try {
				// If the application has the appropriate access then a token
				// will be retrieved, otherwise
				// an error will be thrown.
				GoogleAccountCredential credential = GoogleAccountCredential
						.usingAudience(SignInActivity.this,
								AppConstants.AUDIENCE);
				credential.setSelectedAccountName(emailAccount);

				String accessToken = credential.getToken();

				Log.d(LOG_TAG, "AccessToken retrieved");

				// Success.
				return true;
			} catch (GoogleAuthException unrecoverableException) {
				Log.e(LOG_TAG, "Exception checking OAuth2 authentication.",
						unrecoverableException);
				// Failure.
				return false;
			} catch (IOException ioException) {
				Log.e(LOG_TAG, "Exception checking OAuth2 authentication.",
						ioException);
				// Failure or cancel request.
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... stringIds) {
			// Toast only the most recent.
			Integer stringId = stringIds[0];
			Toast.makeText(SignInActivity.this, stringId, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected void onPreExecute() {
			mAuthTask = this;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			TextView emailAddressTV = (TextView) SignInActivity.this
					.findViewById(R.id.email_address_tv);
			if (success) {
				// Authorization check successful, set internal variable.
				mEmailAccount = emailAddressTV.getText().toString();
			} else {
				// Authorization check unsuccessful, reset TextView to empty.
				emailAddressTV.setText("");
			}
			mAuthTask = null;
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}

}
