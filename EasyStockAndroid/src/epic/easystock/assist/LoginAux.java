package epic.easystock.assist;

import java.io.IOException;

import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

import epic.easystock.activitys.HomeActivity;
import epic.easystock.activitys.LoginActivity;

public class LoginAux implements ConnectionCallbacks,
OnConnectionFailedListener, OnAccessRevokedListener{
	
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	public GoogleApiClient getmGoogleApiClient() {
		return mGoogleApiClient;
	}


	/*
	 * Store the connection result from onConnectionFailed callbacks so that we
	 * can resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;
	/*
	 * Track whether the sign-in button has been clicked so that we know to
	 * resolve all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;
	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	
	private View signView;
	private Context context;
	
	private OnClickListener signInOnClickListener;
	
	public LoginAux(View signView,Context context) {
		this.signView = signView;
		this.context = context;
		init();
		signView.setOnClickListener(signInOnClickListener);

		mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, null).addScope(Plus.SCOPE_PLUS_PROFILE).build();
	}
	private void init(){
		signInOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (!mGoogleApiClient.isConnecting()) {
					if (mConnectionResult != null) {
						mSignInClicked = true;
						resolveSignInError();
					} else
						mGoogleApiClient.connect();
				}
			}
		};
	}
	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(new HomeActivity(), RC_SIGN_IN); //FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	
	
	private class ooo extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String token = "";
			String s = Plus.AccountApi.getAccountName(mGoogleApiClient);

			final String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
			// final String USERINFO_SCOPE =
			// "https://www.googleapis.com/auth/userinfo.profile";
			// final String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE;

			try {
				token = GoogleAuthUtil.getToken(context, s, G_PLUS_SCOPE);
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
			Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}

	}

}
