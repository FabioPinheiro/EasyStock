package epic.easystock.activitys;

import epic.easystock.MainActivity;
import epic.easystock.R;
import epic.easystock.RegisterActivity;
import epic.easystock.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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
	
	public void changeToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
