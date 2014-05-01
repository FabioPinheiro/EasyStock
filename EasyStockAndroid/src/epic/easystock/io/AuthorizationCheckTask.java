package epic.easystock.io;

import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.assist.AppConstants;

public class AuthorizationCheckTask extends AsyncTask<Void, Integer, Boolean> {
	static private AuthorizationCheckTask mAuthTask;// FIXME REMOVE !!!
	
	static public void performAuthCheck(Context context, String[] emailAccounts) {
		// Cancel previously running tasks.
		if (mAuthTask != null) {
			try {
				mAuthTask.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		new AuthorizationCheckTask(context, emailAccounts).execute();
	}
	
	private Context context;
	private String[] emailAccounts;// XXX devia passar pelo AsyncTask

	AuthorizationCheckTask(Context context, String[] emailAccounts) {
		this.emailAccounts = emailAccounts;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Void... v) {
		Log.i(AuthorizationCheckTask.class.getCanonicalName(), "Background task started.");

		if (!AppConstants.checkGooglePlayServicesAvailable(context)) {
			Log.e(AuthorizationCheckTask.class.getCanonicalName(),"Fail to checkGooglePlayServicesAvailable: activity=" + context.toString());
			return false;
		}

		String emailAccount = emailAccounts[0];
		// Ensure only one task is running at a time.
		mAuthTask = this;

		// Ensure an email was selected.
		if (Strings.isNullOrEmpty(emailAccount)) {
			Log.e(AuthorizationCheckTask.class.getCanonicalName(),"isNullOrEmpty(emailAccount)="+ emailAccount);
			return false;
		}

		Log.d(AuthorizationCheckTask.class.getCanonicalName(), "Attempting to get AuthToken for account: " + emailAccount);

		try {
			// If the application has the appropriate access then a token
			// will be retrieved, otherwise
			// an error will be thrown.
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(context, AppConstants.AUDIENCE);
			credential.setSelectedAccountName(emailAccount);

			String accessToken = credential.getToken(); //FIXME REMOVE

			Log.d(AuthorizationCheckTask.class.getCanonicalName(), "AccessToken retrieved");

			// Success.
			return true;
		} catch (GoogleAuthException unrecoverableException) {
			Log.e(AuthorizationCheckTask.class.getCanonicalName(), "Exception checking OAuth2 authentication.",
					unrecoverableException);
			// Failure.
			return false;
		} catch (IOException ioException) {
			Log.e(AuthorizationCheckTask.class.getCanonicalName(), "Exception checking OAuth2 authentication.",
					ioException);
			// Failure or cancel request.
			return false;
		}
	}

	@Override
	protected void onProgressUpdate(Integer... stringIds) {
		// Toast only the most recent.
		Integer stringId = stringIds[0];
		Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
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
		super.onCancelled(); // Now make more sense
		mAuthTask = null;
	}
}
