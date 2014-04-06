package epic.easystock.activitys;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, OnClickListener, OnAccessRevokedListener {

	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_login);
		findViewById(epic.easystock.R.id.sign_in_button).setOnClickListener(
				this);
		// Connect our sign in, sign out and disconnect buttons.
		findViewById(epic.easystock.R.id.sign_in_button).setOnClickListener(
				this);
		findViewById(epic.easystock.R.id.sign_out_button).setOnClickListener(
				this);
		findViewById(epic.easystock.R.id.revoke_access_button)
				.setOnClickListener(this);
		findViewById(epic.easystock.R.id.sign_out_button).setVisibility(
				View.INVISIBLE);
		findViewById(epic.easystock.R.id.revoke_access_button).setVisibility(
				View.INVISIBLE);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();
	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/*
	 * Track whether the sign-in button has been clicked so that we know to
	 * resolve all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/*
	 * Store the connection result from onConnectionFailed callbacks so that we
	 * can resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;

	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the
			// user clicks
			// 'sign-in'.
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;

		// Hide the sign in button, show the sign out buttons.
		findViewById(epic.easystock.R.id.sign_in_button).setVisibility(
				View.INVISIBLE);
		findViewById(epic.easystock.R.id.sign_out_button).setVisibility(
				View.VISIBLE);
		findViewById(epic.easystock.R.id.revoke_access_button).setVisibility(
				View.VISIBLE);

		Person p;
		if ((p = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)) != null) {
			new AsyncTask<Person, Void, Drawable>() {

				@Override
				protected Drawable doInBackground(Person... params) {
					Person p = params[0];
					Drawable d = null;
					try {
						InputStream is = (InputStream) new URL(p.getImage()
								.getUrl()).getContent();
						d = Drawable.createFromStream(is, "");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return d;
				}

				@Override
				protected void onPostExecute(Drawable result) {
					super.onPostExecute(result);
					ImageView iv = (ImageView) findViewById(epic.easystock.R.id.imageView1);
					iv.setImageDrawable(result);
				}

			}.execute(p);
		}
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// GoogleAuthUtil.getToken(context, accountName, scope);

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String token = "";
				String s = Plus.AccountApi.getAccountName(mGoogleApiClient);

				final String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
				// final String USERINFO_SCOPE =
				// "https://www.googleapis.com/auth/userinfo.profile";
				// final String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE;

				try {
					token = GoogleAuthUtil.getToken(LoginActivity.this, s,
							G_PLUS_SCOPE);
				} catch (UserRecoverableAuthException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					e.printStackTrace();
				}
				return token;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}.execute();

		/*
		 * TextView tv = (TextView)
		 * findViewById(epic.easystock.R.id.logInStatus); tv.setText(s);
		 * tv.setVisibility(View.VISIBLE); Toast.makeText(this, s,
		 * Toast.LENGTH_LONG).show();
		 */
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
	}

	public void onDisconnected() {
		// Bye!
		mGoogleApiClient.disconnect();
		Toast.makeText(this, "User is disconnected!", Toast.LENGTH_LONG).show();
		// Hide the sign out buttons, show the sign in button.
		findViewById(epic.easystock.R.id.sign_in_button).setVisibility(
				View.VISIBLE);
		findViewById(epic.easystock.R.id.sign_out_button).setVisibility(
				View.INVISIBLE);
		findViewById(epic.easystock.R.id.revoke_access_button).setVisibility(
				View.INVISIBLE);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case epic.easystock.R.id.sign_in_button:
			if (!mGoogleApiClient.isConnecting()) {
				if (mConnectionResult != null) {
					mSignInClicked = true;
					resolveSignInError();
				} else
					mGoogleApiClient.connect();
			}
			break;
		case epic.easystock.R.id.sign_out_button:
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				onDisconnected();

			}
			break;
		}

	}

	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// TODO Auto-generated method stub

	}
}