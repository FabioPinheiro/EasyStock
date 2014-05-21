package epic.easystock.activitys;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import epic.easystock.R;
import epic.easystock.assist.PantryAdapter;
import epic.easystock.data.UserBdAdapter.UserPantryAux;
import epic.easystock.io.EndPointCall;

public class MyPantriesActivity extends ListActivity {
	// String mail;
	Button newPantry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pantries);
		TextView myMail = (TextView) findViewById(R.id.myMail);
		myMail.setText(EndPointCall.getEmailAccount());

		PantryAdapter adapter = new PantryAdapter(this, new ArrayList<UserPantryAux>());
		
		setListAdapter(adapter);
		
		newPantry = (Button) findViewById(R.id.createnewpantry);
		newPantry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				xpto();
			}
		});
		
		EndPointCall.listAllPantriesTask(adapter);
	}
	public void xpto() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New Pantry in " + EndPointCall.getEmailAccount()); // TEXT
																			// FIXME
		alert.setMessage("Chose the Pantry Name:"); // TEXT FIXME
		final EditText textView = new EditText(this);
		alert.setView(textView);
		alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EndPointCall.newPantryTask(textView.getText().toString());
			}
		});
		alert.show();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		UserPantryAux pantry = (UserPantryAux) getListAdapter().getItem(position);
		Intent intent = new Intent(this, PantyActivity.class);
		intent.putExtra("PANTRYNAME", pantry.pantryName);
		startActivity(intent);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_pantries, menu);
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
}
