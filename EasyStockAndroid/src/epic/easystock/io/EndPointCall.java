package epic.easystock.io;

import java.util.List;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.assist.ProductAdapter;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalProduct;
import epic.easystock.data.PantriesDbAdapter;
import epic.easystock.data.PantriesDbAdapter.PantryDB;
import epic.easystock.data.ProductsDbAdapter;
import epic.easystock.data.UserBdAdapter;

public class EndPointCall {
	static private final String EndPointCall_TAG = "EndPointCall";
	// static private final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;
	// //FIXME TODO
	static private final String PREFS_FILE_NAME = "EasyStockPrefsFile";
	static private final String PREFS_FIRST_INIT = "PREFS_FIRST_INIT";
	static private final String PREFS_LAST_USED_EMAIL = "PREFS_LAST_USED_EMAIL";
	static private final String PREFS_LAST_USED_PANTRY = "PREFS_LAST_USED_PANTRY";
	static private final String PREFS_NEXT_LOCAL_KEY = "PREFS_NEXT_LOCAL_KEY";
	
	static private final String ERROR_PANTRY_NAME = "ERROR_PANTRY_NAME";

	static public final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static public final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static public final String FAIL_TO_LOAD_PANTRY = "FAIL_TO_LOAD_PANTRY";
	static public final String FAIL_TO_LOAD_PRODUCT = "FAIL_TO_LOAD_PRODUCT";
	static public final String FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME = "FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME";
	static public final String FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER = "FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER";
	static public final String FAIL_PRODUCT_ALREADY_IN_THE_PANTRY = "FAIL_PRODUCT_ALREADY_IN_THE_PANTRY";

	static public final String INSERT_NEW_USER_IN_APPENGINE = "INSERT_NEW_USER_IN_APPENGINE";
	static public final String PANTRY_IS_EMPTY = "PANTRY_IS_EMPTY";
	public static final String PRODUCT_ADDED_TO_LOCAL_PANTRY = "PRODUCT_ADDED_TO_LOCAL_PANTRY";
	public static final String PANTRY_IS_ALREADY_OPEN = "PANTRY_IS_ALREADY_OPEN";

	static public final String DONE = "AsyncTask Done"; // FIXME remove!!! use
	static public final String DEBUG = "DEBUG";
	static public final String ERROR = "ERROR";
														// in debug
	static private Context globalContext = null;
	static private SharedPreferences globalSettings;
	static private String mEmailAccount = null;
	
	static public Context getGlobalContext() {
		return globalContext;
	}
	static public String getEmailAccount() {
		return mEmailAccount;
	}
	static public User getUser() {
		User user = new User();
		user.setEmail(EndPointCall.getEmailAccount());
		String[] uMail = EndPointCall.getEmailAccount().split("@");
		user.setNick(uMail[0]);
		return user;
	}
	// static protected void setEmailAccount(String account) {mEmailAccount=account;}
	static public boolean isSignedIn() {
		return (!Strings.isNullOrEmpty(mEmailAccount) ? true : false);
	}
	public static PantriesDbAdapter pantriesDbAdapter =null;
	public static PantriesDbAdapter.PantryDB getPantryDB(String selectedPantry) {
		if(Strings.isNullOrEmpty(selectedPantry)){
			msg(EndPointCall_TAG,"getSelectedPantry is null or empty"); //FIXME TEXT
			Log.e(EndPointCall_TAG, "getSelectedPantry is null or empty"); //FIXME TEXT
			throw new RuntimeException();
		}else{
			globalSettings.edit().putString(PREFS_LAST_USED_PANTRY, selectedPantry).commit();
			if(pantriesDbAdapter == null){
				throw new RuntimeException();
			}
			return pantriesDbAdapter.getPantryDB(selectedPantry);
		}
	}
	public static String getPantryName() {
		String aux = globalSettings.getString(PREFS_LAST_USED_PANTRY, ERROR_PANTRY_NAME);
		if(Strings.isNullOrEmpty(aux)){
			msg(EndPointCall_TAG,"getPantryName is null or empty"); //FIXME TEXT
			Log.e(EndPointCall_TAG, "getPantryName is null or empty"); //FIXME TEXT
			throw new RuntimeException();
		}
		return aux;
	}
	/*LIXO FIXME UHWEKFHWKJFHLWJI public static PantryDbAdapter getPantryDbAdapteri() {
		if(pantryDbAdapter == null) throw new RuntimeException(); //FIXME
		return pantryDbAdapter;
	}*/
	private static UserBdAdapter userBdAdapter = null; //FIXME falta close   (o open esta no init)
	public static UserBdAdapter getUserDbAdapter() {
		if(userBdAdapter == null) throw new RuntimeException(); //FIXME
		return userBdAdapter;
	}
	private static ProductsDbAdapter productsDbAdapter = null; //FIXME falta close   (o open esta no init)
	public static ProductsDbAdapter getProductsDbAdapter() {
		if(productsDbAdapter == null) throw new RuntimeException(); //FIXME
		return productsDbAdapter;
	}
	public static Long xptonextLocalObjectKey() {
		long aux = globalSettings.getLong(PREFS_NEXT_LOCAL_KEY, 0);
		globalSettings.edit().putLong(PREFS_NEXT_LOCAL_KEY, ++aux).commit(); //FIXME sera que alguem consege chegar ao fim?
		if(aux != 0)
			return aux;
		else throw new RuntimeException();
	}
	// ##################################################################################
	public static boolean isConnected() {
		boolean connected = false;
		ConnectivityManager conMan = (ConnectivityManager) EndPointCall.getGlobalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ? null : conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // mobile
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null ? null : conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // wifi
		if (mobile != null && (mobile == State.CONNECTED || mobile == State.CONNECTING)) {
			connected = true;
		}
		if (wifi != null && (wifi == State.CONNECTED || wifi == State.CONNECTING)) {
			connected = true;
		}
		return connected;
	}
	static public void msg(String message) {
		Toast.makeText(EndPointCall.getGlobalContext(), message, Toast.LENGTH_LONG).show();
	}
	static public void msg(String TAG, String message) {
		msg(TAG + ":" + message); // FIXME tag is for debug
	}
	static public void msgNotSignedIn() {
		msg("Need To Sign In");
	} // FIXME TEXT
	static public void msgAddProductToPantry() {
		msg("Product add to Pantry");
	} // FIXME TEXT
	static public void onApplicationCreate(Context context) {
		Log.i(EndPointCall_TAG, "onApplicationCreate");
		globalContext = context;
		globalSettings = globalContext.getSharedPreferences(PREFS_FILE_NAME, 0);
		boolean fistTime = globalSettings.getBoolean(PREFS_FIRST_INIT, true);
		// Select Email
		productsDbAdapter = new ProductsDbAdapter(EndPointCall.getGlobalContext());
		productsDbAdapter.open();
		if (fistTime) {
			globalSettings.edit().putBoolean(PREFS_FIRST_INIT, false).commit();
			Account aux = new AccountSelector(getGlobalContext()).selectorAccount();
			globalSettings.edit().putString(PREFS_LAST_USED_EMAIL, aux.name).commit();// FIXME
			globalSettings.edit().putLong(PREFS_NEXT_LOCAL_KEY, 1).commit();
			mEmailAccount = aux.name;
			listAllProductTask(null);
			msg("Hello " + mEmailAccount);
		} else {
			mEmailAccount = globalSettings.getString(PREFS_LAST_USED_EMAIL, "EMAIL_ERROR");
			msg("Welcome Back " + mEmailAccount);// FIXME TEXT
		}
		userBdAdapter = new UserBdAdapter(EndPointCall.getGlobalContext());
		userBdAdapter.open();
		pantriesDbAdapter = new PantriesDbAdapter(EndPointCall.getGlobalContext());
	}
	static public void onInit(Activity activity) {
		Log.i(EndPointCall_TAG, "onInit()");
		// TODO
	}
	static public ApiEndpoint getApiEndpoint() {
		GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(EndPointCall.getGlobalContext(), AppConstants.AUDIENCE);
		credential.setSelectedAccountName(EndPointCall.getEmailAccount());
		ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);
		return endpoint;
	}
	/*
	 * public static ApiEndpoint getApiEndpoint() { FIXME apiEndpointBuilder = new ApiEndpoint.Builder( AndroidHttp.newCompatibleTransport(), new JacksonFactory(), new HttpRequestInitializer() { public void initialize(HttpRequest httpRequest) {} }); apiEndpoint = CloudEndpointUtils.updateBuilder(apiEndpointBuilder).build(); return apiEndpoint; }
	 */
	// ##################################################################################
	static public void addToProductListTask(String name, Long barCode, String description, String image) {
		new AddToProductListTask(name, barCode, description, image).execute();
	}
	static public void addProductToPantryTask(MetaProductAdapter adapter, PantriesDbAdapter.PantryDB pantryDB, Long productBarCode) {
		ProductsDbAdapter productsDBAdapter = EndPointCall.getProductsDbAdapter();
		List<LocalProduct> ooo = productsDBAdapter.getProductByBarCode(productBarCode);
		if(productsDBAdapter.getAllProducts().isEmpty())
			throw new RuntimeException(); //FIXME LIXO REMOVE
		//ver na local BD
		if(ooo.isEmpty() && isConnected()){
			//FIXME if not ver na AppEngine
		}else{
			//NONE ??!?!? FIXME
		}
		//if not perguntar se quer criar um, NAO QUER PORQUE EU NAO DEIXO, POR AGORA FIXME 
		//if not exit FIXME

		if(ooo.isEmpty()){
			msg(EndPointCall_TAG, "Unknown Product");
			/*FIXME AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Pro");
			alert.setItems(pantreisName, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedPantryName = pantreisName[which];//FIXME
					final PantriesDbAdapter.PantryDB pantryDB = EndPointCall.getPantryDB(selectedPantryName);
					EndPointCall.listPantryProductTask(adapter, pantryDB); //FIXME
				}
			});
			final Activity aux = this;
			alert.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					aux.finish();
				}
			});
			alert.show();*/
		}
		else{
			if(ooo.size() == 1)
				new AddProductToLocalPantryTask(adapter, pantryDB, new LocalMetaProduct(ooo.get(0),2.2)).execute();
			else new RuntimeException(); //FIXME
		}
		//new AddProductToPantryTask(adapter, selectedPantry, productId).execute(); // FIXME getSelectedPantry()

	}
	static public void listAllProductTask(ProductAdapter adapter) {
		new ListAllProductTask(adapter).execute();
	}
	static public void newPantryTask(String pantryName) {
		new NewPantryTask(pantryName).execute();
	}
	static public void listPantryProductTask(MetaProductAdapter adapter, PantriesDbAdapter.PantryDB pantryDB) {
		Log.i(EndPointCall_TAG, "isConnected()=" + isConnected());
		if (isConnected()) {
			new ListPantryProductTask(adapter, pantryDB, EndPointCall.getPantryName()).execute();
		} else {
			adapter.addAll(pantryDB.getAllProducts());
			adapter.notifyDataSetChanged();
			// throw new UnsupportedOperationException();
			// FIXME new ListPantrysTask(adapter).execute();
		}
	}
	static public void SynchronizePantry(PantryDB pantryDB) {
		if(isConnected()){
			new SynchronizePantryTask(pantryDB);
		}else{
			msg(EndPointCall_TAG, "SynchronizePantry_FAIL"); //FIXME TEXT e ver estado
		}
	}
}
