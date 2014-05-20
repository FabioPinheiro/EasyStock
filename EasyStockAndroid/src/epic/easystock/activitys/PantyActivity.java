package epic.easystock.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import epic.easystock.R;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.PantriesDBAdapter;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.io.EndPointCall;
import android.content.Intent;

public class PantyActivity extends ListActivity {
	protected static final int ADDPRODUCT = 666;
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private PantriesDBAdapter.PantryDB pantryDB;
	String mail;
	Button addProduct;
	MetaProductAdapter adapter;
	String selectedPantryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		//LIXO mail = getIntent().getStringExtra("MAIL");
		mail = EndPointCall.getEmailAccount();//FIXME
		addProduct = (Button) findViewById(R.id.AddProduct);
		adapter = new MetaProductAdapter(this, new ArrayList<LocalMetaProduct>());
		setListAdapter(adapter);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final String[] pantreisName = EndPointCall.getUserDBAdapter().avalablePantrysNamesFromUser(EndPointCall.getEmailAccount());
		alert.setTitle("Select Pantry");
		alert.setItems(pantreisName, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedPantryName = pantreisName[which];//FIXME
				pantryDB = EndPointCall.getPantryDB(selectedPantryName);
				EndPointCall.listPantryProductTask(adapter, pantryDB); //FIXME
				
				//NÂO gosto
			}
		});
		final Activity aux = this;
		alert.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				aux.finish();
			}
		});
		alert.show();
		
		addProduct.setOnClickListener(new OnClickListener() {
			Intent intent = new Intent(EndPointCall.getGlobalContext(),AddProductToPantryActivity.class);
			@Override
			public void onClick(View v) {
				intent.putExtra("PANTRYNAME", selectedPantryName);
				startActivityForResult(intent, ADDPRODUCT);
	/*			
				Log.i(LOG_TAG, "new AddProductTask: " + mail);
				Long productBarCode = Long.valueOf(((EditText) findViewById(R.id.NumberId)).getText().toString());
				EndPointCall.addProductToPantryTask(adapter, pantryDB, productBarCode);//FIXME BARCODE*/
			}
		});
		
		
		final AlertDialog.Builder addproduct = new AlertDialog.Builder(this);
		addproduct.setTitle("Change Product");
		addproduct.setItems(pantreisName, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedPantryName = pantreisName[which];//FIXME
				pantryDB = EndPointCall.getPantryDB(selectedPantryName);
				EndPointCall.listPantryProductTask(adapter, pantryDB); //FIXME
				
				//NÂO gosto
			}
		});
		
		addproduct.setNeutralButton("new",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		addproduct.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				aux.finish();
			}
		});
	/*	
		addProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addproduct.show();
				
				Log.i(LOG_TAG, "new AddProductTask: " + mail);
				Long productBarCode = Long.valueOf(((EditText) findViewById(R.id.NumberId)).getText().toString());
				EndPointCall.addProductToPantryTask(adapter, pantryDB, productBarCode);//FIXME BARCODE
			}
		});*/
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == ADDPRODUCT) {
	        if(resultCode == RESULT_OK){
	            String name=data.getStringExtra("PRODNAME");
	            String description=data.getStringExtra("PRODDESCRIPTION");
	            Long barCode=data.getLongExtra("PRODBARCODE", -1l);
				Product result = new Product();
				result.setBarCode(barCode);
				result.setDescription(description);
				result.setName(name);
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}//onActivityResult
	@Override
	protected void onStop() {
		super.onStop();
		EndPointCall.SynchronizePantry(pantryDB);
	}
}
