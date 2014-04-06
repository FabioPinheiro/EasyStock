package epic.easystock.activitys;

import com.google.android.gms.common.api.GoogleApiClient;

import epic.easystock.MainActivity;
import epic.easystock.R;
import epic.easystock.RegisterActivity;
import epic.easystock.R.layout;
import epic.easystock.assist.LoginAux;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class HomeActivity extends Activity {
	LoginAux loginAux;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		View signView = findViewById(epic.easystock.R.id.sign_in_button);
		Context context = this.getApplicationContext();
		loginAux = new LoginAux(signView,context);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loginAux.getmGoogleApiClient().connect();
	}
	@Override
	protected void onStop() {
		super.onStop();

		if (loginAux.getmGoogleApiClient().isConnected()) {
			loginAux.getmGoogleApiClient().disconnect();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void changeToProductListActivity(View view) {
		Intent intent = new Intent(this, ProductListActivity.class);
		startActivity(intent);
	}
	
	public void changeRegisterActivity(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	public void changeToTestAddToProductListActivity(View view) {
		Intent intent = new Intent(this, TestAddToProductListActivity.class);
		startActivity(intent);
	}
	
	public void changeToPantyActivity(View view) {
		Intent intent = new Intent(this, PantyActivity.class);
		startActivity(intent);
	}
	public void changeToLogInActivity(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	public void changeToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
