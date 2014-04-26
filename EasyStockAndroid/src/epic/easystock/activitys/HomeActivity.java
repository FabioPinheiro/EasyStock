package epic.easystock.activitys;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;

import epic.easystock.MainActivity;
import epic.easystock.R;
import epic.easystock.assist.SignInAux;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.internal.in;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

public class HomeActivity extends Activity {
	String mail;

	// Context applicationContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_home);
		mail = getIntent().getStringExtra("MAIL");

	}

	private boolean isSignedIn() {
		if (!Strings.isNullOrEmpty(mail)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void signIn(View view) {
		Toast.makeText(this, "signIn HOME", Toast.LENGTH_SHORT);
		String myMail = new SignInAux().signIn(view, this);
		Toast.makeText(this, "signIn HOME" + myMail, Toast.LENGTH_SHORT);
	}


	public void changeToProductListActivity(View view) {
		Intent intent = new Intent(this, ProductListActivity.class);
		if (isSignedIn()) {
			intent.putExtra("MAIL", mail);
			startActivity(intent);
		}
	}

	/*
	 * public void changeRegisterActivity(View view) { Intent intent = new
	 * Intent(this, RegisterActivity.class); intent.putExtra("MAIL", mail);
	 * startActivity(intent); }
	 */

	public void changeToTestAddToProductListActivity(View view) {
		Intent intent = new Intent(this, TestAddToProductListActivity.class);
		intent.putExtra("MAIL", mail);
		startActivity(intent);
	}

	public void changeToPantyActivity(View view) {
		Intent intent = new Intent(this, PantyActivity.class);
		intent.putExtra("MAIL", mail);
		startActivity(intent);
	}

	/*
	 * public void changeToLogInActivity(View view) { Intent intent = new
	 * Intent(this, SignInActivity.class); intent.putExtra("MAIL", mail);
	 * startActivity(intent); }
	 */

	public void changeToMyPantriesActivity(View view) {
		Intent intent = new Intent(this, MyPantriesActivity.class);
		intent.putExtra("MAIL", mail);
		startActivity(intent);
	}

	public void changeToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("MAIL", mail);
		startActivity(intent);
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
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_pantries, menu);
		return true;
	}

}
