package epic.easystock.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import epic.easystock.R;
import epic.easystock.assist.SignInAux;
import epic.easystock.assist.endPointCall.EndPointCall;

public class HomeActivity extends Activity {
	//String mail;

	// Context applicationContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EndPointCall.onCreate(this);
		setTitle(R.string.title_activity_home);
		setContentView(epic.easystock.R.layout.activity_home);
		//mail = getIntent().getStringExtra("MAIL");
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
		if (EndPointCall.isSignedIn()) { //FIXME I don't like
			//LIXO intent.putExtra("MAIL", mail);
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
		//LIXO intent.putExtra("MAIL", mail);
		startActivity(intent);
	}

	public void changeToPantyActivity(View view) {
		Intent intent = new Intent(this, PantyActivity.class);
		//LIXO intent.putExtra("MAIL", mail);
		startActivity(intent);
	}

	/*
	 * public void changeToLogInActivity(View view) { Intent intent = new
	 * Intent(this, SignInActivity.class); intent.putExtra("MAIL", mail);
	 * startActivity(intent); }
	 */

	public void changeToMyPantriesActivity(View view) {
		Intent intent = new Intent(this, MyPantriesActivity.class);
		//LIXO intent.putExtra("MAIL", mail);
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
