package epic.easystock.activitys;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.api.client.util.Strings;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
	Button readBarcode;
	Button addProduct;
	MetaProductAdapter adapter;
	String selectedPantryName;
	private View xpto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(epic.easystock.R.layout.activity_panty);
		// LIXO mail = getIntent().getStringExtra("MAIL");
		mail = EndPointCall.getEmailAccount();// FIXME
		addProduct = (Button) findViewById(R.id.AddProduct);
		readBarcode = (Button) findViewById(R.id.read_bar_code_button);
		adapter = new MetaProductAdapter(this,
		new ArrayList<LocalMetaProduct>());
		xpto = findViewById(R.id.layout_edit_botons_pantry_list);
		setListAdapter(adapter);
		selectedPantryName = getIntent().getStringExtra("PANTRYNAME");
		getIntent().removeExtra("PANTRYNAME");
		final String[] pantreisName = EndPointCall.getUserDBAdapter().avalablePantrysNamesFromUser(EndPointCall.getEmailAccount());
		// AlertDialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Select Pantry");
		alert.setItems(pantreisName, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedPantryName = pantreisName[which];// FIXME
				pantryDB = EndPointCall.getPantryDB(selectedPantryName);
				EndPointCall.listPantryProductTask(adapter, pantryDB); // FIXME
				// NÂO gosto
			}
		});
		final Activity aux = this;
		alert.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				aux.finish();
			}
		});
		if	(pantreisName.length == 0){ //FIXME (FABIO) não gosto disto 
			EndPointCall.msg(LOG_TAG, EndPointCall.NO_PANTRY); 
			this.finish();
		}
		else if (Strings.isNullOrEmpty(selectedPantryName) && pantreisName.length > 1)
			alert.show();
		else {
			if (Strings.isNullOrEmpty(selectedPantryName) && pantreisName.length == 1)
				selectedPantryName = pantreisName[0];
			pantryDB = EndPointCall.getPantryDB(selectedPantryName);
			EndPointCall.listPantryProductTask(adapter, pantryDB);
		}
		addProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String barcodeStr = ((EditText) findViewById(R.id.NumberId))
				.getText().toString();
				if (Strings.isNullOrEmpty(barcodeStr))
					return;
				Long barcode = Long.valueOf(barcodeStr);
				Log.i(LOG_TAG, "Adding product with barcode: " + barcode);
				EndPointCall.addProductToPantryTask(adapter, pantryDB, barcode);
			}
		});
		readBarcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchBarcode();
			}
		});
		final AlertDialog.Builder detailsAlert = new AlertDialog.Builder(this);
		detailsAlert.setTitle("Product Details");
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
		new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
			View v, int position, long id) {
				LocalMetaProduct product = (LocalMetaProduct) getListAdapter()
				.getItem(position);
				String[] details = { product.getName(),
				product.getDescription(),
				product.getAmount() + " Items", "Barcode: " + product.getBarCode() };
				detailsAlert.setItems(details,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
					int which) {}
				});
				detailsAlert.show();
				return false;
			}
		});
	}
	
	private void dispatchBarcode() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final LocalMetaProduct product = (LocalMetaProduct) getListAdapter()
		.getItem(position);
		// Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		xpto.setVisibility(View.VISIBLE);
		TextView x = (TextView) findViewById(R.id.selectProduct);
		x.setText(product.getName());
		Button a1 /* add one */= (Button) findViewById(R.id.buttonAddOne);
		Button r1 /* remove one */= (Button) findViewById(R.id.buttonRemoveOne);
		a1.setOnClickListener(new OnClickListener() {
			Integer number;
			
			@Override
			public void onClick(View v) {
				String str = ((EditText) findViewById(R.id.editNumber))
				.getText().toString();
				if (!Strings.isNullOrEmpty(str))
					number = Integer
					.valueOf(((EditText) findViewById(R.id.editNumber))
					.getText().toString());
				else
					number = 1;
				DecimalFormat twoDForm = new DecimalFormat("#.00");
				product.setAmount(Double.valueOf(twoDForm.format(product
				.getAmount().intValue() + number)));
				EndPointCall.plusOneOnProductAmoutTask(product, pantryDB);
				adapter.clear();
				adapter.addAll(pantryDB.getAllProducts());
			}
		});
		r1.setOnClickListener(new OnClickListener() {
			Integer number;
			
			@Override
			public void onClick(View v) {
				String str = ((EditText) findViewById(R.id.editNumber))
				.getText().toString();
				if (!Strings.isNullOrEmpty(str))
					number = Integer
					.valueOf(((EditText) findViewById(R.id.editNumber))
					.getText().toString());
				else
					number = 1;
				if (product.getAmount() >= number) {
					product.setAmount(product.getAmount() - number);
					EndPointCall.plusOneOnProductAmoutTask(product, pantryDB);
				} else
					Toast.makeText(EndPointCall.getGlobalContext(),
					"AMOUNT IS TO LOW", Toast.LENGTH_SHORT).show();
				adapter.clear();
				adapter.addAll(pantryDB.getAllProducts());
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
		requestCode, resultCode, data);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			EditText barcode = (EditText) findViewById(R.id.NumberId);
			barcode.setText(scanContent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
			"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}// onActivityResult
	
	@Override
	protected void onStop() {
		super.onStop();
		EndPointCall.SynchronizePantry(pantryDB);
	}
}
