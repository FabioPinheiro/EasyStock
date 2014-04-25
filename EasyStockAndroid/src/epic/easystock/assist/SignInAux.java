package epic.easystock.assist;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.activitys.SignInActivity;

public class SignInAux {

	private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;

	public static final String LOG_TAG = "SignInActivity:";

	private AuthorizationCheckTask mAuthTask;
	private String mEmailAccount = "";
	private com.google.android.gms.common.SignInButton button;

	public String signIn(View view, Activity context) {
		String mEmailAccount = "";

		int googleAccounts = AppConstants.countGoogleAccounts(context);
		if (googleAccounts == 0) {
			// No accounts registered, nothing to do.
			Toast.makeText(context, "no_google_accounts_registered",
					Toast.LENGTH_LONG).show();
		} else if (googleAccounts == 1) {
			// If only one account then select it.
			AccountManager am = AccountManager.get(context);
			Account[] accounts = am
					.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			if (accounts != null && accounts.length > 0) {
				// Select account and perform authorization check.
				mEmailAccount = accounts[0].name;
				performAuthCheck(accounts[0].name, context);
			}
		} else {
			/*
			 * // More than one Google Account is present, a chooser is
			 * necessary.
			 * 
			 * // Reset selected account.
			 * 
			 * // Invoke an {@code Intent} to allow the user to select a Google
			 * // account. Intent accountSelector =
			 * AccountPicker.newChooseAccountIntent(null, null, new String[] {
			 * GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false,
			 * "Select the account to access the EasyStock API.", null, null,
			 * null); startActivityForResult(accountSelector,
			 * ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
			 */
		}
		return mEmailAccount;
	}

	private void performAuthCheck(String emailAccount, Activity context) {
		// Cancel previously running tasks.
		if (mAuthTask != null) {
			try {
				mAuthTask.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		new AuthorizationCheckTask(context).execute(emailAccount);
	}

	class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean> {
		Activity context;
		
		public AuthorizationCheckTask(Activity context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(String... emailAccounts) {
			Log.i(LOG_TAG, "Background task started.");

			if (!AppConstants
					.checkGooglePlayServicesAvailable(context)) {
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
						.usingAudience(context,
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
			Toast.makeText(context, stringId, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected void onPreExecute() {
			mAuthTask = this;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			mAuthTask = null;
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}

}
