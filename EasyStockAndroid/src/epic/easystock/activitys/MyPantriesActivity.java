package epic.easystock.activitys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.ApiEndpoint.InsertUserPantry;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.apiEndpoint.model.UserPantry;
import epic.easystock.apiEndpoint.model.UserPantryDTO;
import epic.easystock.assist.AppConstants;
import epic.easystock.io.AddProductToPantryTask;
import epic.easystock.io.EndPointCall;

public class MyPantriesActivity extends Activity {
	// String mail;
	Button newPantry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pantries);
		TextView myMail = (TextView) findViewById(R.id.myMail);
		myMail.setText(EndPointCall.getEmailAccount());

		//FIXME final String[] pantreisName = EndPointCall.getUserDbAdapter().avalablePantrysNamesFromUser(EndPointCall.getEmailAccount());
		
		newPantry = (Button) findViewById(R.id.createnewpantry);
		newPantry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				xpto();
			}
		});
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
