package epic.easystock.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import epic.easystock.R;
import epic.easystock.io.EndPointCall;
import android.graphics.Typeface;


public class HomeActivity extends Activity {
	//String mail;

	@Override
	public void startActivity(Intent intent) {
		if (EndPointCall.isSignedIn()) {
			super.startActivity(intent);
		}else{
			EndPointCall.msgNotSignedIn();
		}
	}

	// Context applicationContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_home);
		
		Typeface blockFonts = Typeface.createFromAsset(getAssets(),"FuturaLight.ttf");
		TextView fancyPantry = (TextView) findViewById(epic.easystock.R.id.FancyPantry);
		Button myPantries = (Button) findViewById(epic.easystock.R.id.my_pantries);
		Button pantryActivity = (Button) findViewById(epic.easystock.R.id.pantry_activity);
		Button productList = (Button) findViewById(epic.easystock.R.id.product_list);
		Button addProduct = (Button) findViewById(epic.easystock.R.id.add_product);
		fancyPantry.setTypeface(blockFonts);
		myPantries.setTypeface(blockFonts);
		pantryActivity.setTypeface(blockFonts);
		productList.setTypeface(blockFonts);
		addProduct.setTypeface(blockFonts);
		
		EndPointCall.onInit(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void changeToProductListActivity(View view) {
		Intent intent = new Intent(this, ProductListActivity.class);
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

	public void changeToMyPantriesActivity(View view) {
		Intent intent = new Intent(this, MyPantriesActivity.class);
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
