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

import com.google.android.gms.internal.db;
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
import epic.easystock.data.PantriesDBAdapter;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.data.UserBdAdapter.UserPantryAux;
import epic.easystock.data.ProductsDbAdapter;
import epic.easystock.data.UserBdAdapter;

public class EndPointCall {
	static private final String EndPointCall_TAG = "EndPointCall";
	// static private final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222; //FIXME TODO
	//PREFS
	static private final String PREFS_FILE_NAME = "EasyStockPrefsFile";
	static private final String PREFS_FIRST_INIT = "PREFS_FIRST_INIT";
	static private final String PREFS_LAST_USED_EMAIL = "PREFS_LAST_USED_EMAIL";
	static private final String PREFS_LAST_USED_PANTRY = "PREFS_LAST_USED_PANTRY";
	static private final String PREFS_NEXT_LOCAL_KEY = "PREFS_NEXT_LOCAL_KEY";
	static private final Long ERROR_PANTRY_KEY = 0l;
	// static private final String ERROR_PANTRY_NAME = "ERROR_PANTRY_NAME";
	
	//FAIL_TAGS
	static public final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static public final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static public final String FAIL_TO_LOAD_PANTRY = "FAIL_TO_LOAD_PANTRY";
	static public final String FAIL_TO_LOAD_PRODUCT = "FAIL_TO_LOAD_PRODUCT";
	static public final String FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME = "FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME";
	static public final String FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER = "FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER";
	static public final String FAIL_PRODUCT_ALREADY_IN_THE_PANTRY = "FAIL_PRODUCT_ALREADY_IN_THE_PANTRY";
	static public final String FAIL_TO_SYNCHRONIZED_PANTRY = "FAIL_TO_SYNCHRONIZED_PANTRY";
	static public final String FAIL_TO_SYNCHRONIZED_USER = "FAIL_TO_SYNCHRONIZED_USER";
	//___TAGS
	static public final String INSERT_NEW_USER_IN_APPENGINE = "INSERT_NEW_USER_IN_APPENGINE";
	static public final String PANTRY_IS_EMPTY = "PANTRY_IS_EMPTY";
	static public final String PRODUCT_ADDED_TO_LOCAL_PANTRY = "PRODUCT_ADDED_TO_LOCAL_PANTRY";
	static public final String PANTRY_IS_ALREADY_OPEN = "PANTRY_IS_ALREADY_OPEN";
	static public final String PANTRY_SYNCHRONIZED = "PANTRY_SYNCHRONIZED";
	static public final String USER_SYNCHRONIZED = "USER_SYNCHRONIZED";
	//DEBUG_TAGS
	static public final String DONE = "AsyncTask Done"; // FIXME remove!!! use
	static public final String DEBUG = "DEBUG";
	static public final String ERROR = "ERROR";
	static private Context globalContext = null;
	static private SharedPreferences globalSettings;
	static private String mEmailAccount = null;
	static private PantriesDBAdapter pantriesDbAdapter = null;	
	static private UserBdAdapter userBdAdapter = null; // FIXME falta close (o open esta no init)
	static private ProductsDbAdapter productsDbAdapter = null; // FIXME falta close (o open esta no init)

	// static protected void setEmailAccount(String account) {mEmailAccount=account;}

	static public Context getGlobalContext() {
		return globalContext;
	}
	
	static public String getEmailAccount() {
		return mEmailAccount;
	}
	
	/*static public User getUser() { //REMOVE 
		User user = new User();
		user.setEmail(EndPointCall.getEmailAccount());
		return user;
	}*/
	
	static public void setLastUsedPantry(long pantryKey) {
		getPantryDB(pantryKey);
	}
	
	static public PantriesDBAdapter.PantryDB getPantryDB(Long pantryKey) {// , String selectedPantry) {
		if (pantryKey == null || pantryKey == 0) {
			msg(EndPointCall_TAG, "getSelectedPantry is null or 0"); // FIXME TEXT
			Log.e(EndPointCall_TAG, "getSelectedPantry is null or 0"); // FIXME TEXT
			throw new RuntimeException();
		}
		else {
			globalSettings.edit().putLong(PREFS_LAST_USED_PANTRY, pantryKey).commit();
			if (pantriesDbAdapter == null) {
				throw new RuntimeException();
			}
			return pantriesDbAdapter.getPantryDB(pantryKey);// , selectedPantry);
		}
	}
	
	static public PantriesDBAdapter.PantryDB getPantryDB(String pantryName) {// , String selectedPantry) {
		if (Strings.isNullOrEmpty(pantryName)) {
			msg(EndPointCall_TAG, "getSelectedPantry is null or empty"); // FIXME TEXT
			Log.e(EndPointCall_TAG, "getSelectedPantry is null or empty"); // FIXME TEXT
			throw new RuntimeException();
		} else {
			long pantryKey = userBdAdapter.getPantryKey(EndPointCall.getEmailAccount(), pantryName);
			return pantriesDbAdapter.getPantryDB(pantryKey);// , selectedPantry);
		}
	}
	
	static public String getPantryNameFromKey(Long pantryKey) {
		return userBdAdapter.getPantryName(pantryKey);
	}
	
	static public String getPantryName() {
		Long aux = globalSettings.getLong(PREFS_LAST_USED_PANTRY, ERROR_PANTRY_KEY);
		if (aux == ERROR_PANTRY_KEY)
			throw new RuntimeException(); // FIXME REMOVE
		return getPantryNameFromKey(aux);
	}
	
	/*
	 * LIXO FIXME UHWEKFHWKJFHLWJI public static PantryDbAdapter getPantryDbAdapteri() { if(pantryDbAdapter == null) throw new RuntimeException(); //FIXME return pantryDbAdapter; }
	 */
	
	static public UserBdAdapter getUserDBAdapter() {
		if (userBdAdapter == null)
			throw new RuntimeException(); // FIXME
		return userBdAdapter;
	}
	
	
	static public ProductsDbAdapter getProductsDbAdapter() {
		if (productsDbAdapter == null)
			throw new RuntimeException(); // FIXME
		return productsDbAdapter;
	}
	
	static public Long xptonextLocalObjectKey() { // LIXO
		long aux = globalSettings.getLong(PREFS_NEXT_LOCAL_KEY, 0);
		globalSettings.edit().putLong(PREFS_NEXT_LOCAL_KEY, ++aux).commit(); // FIXME sera que alguem consege chegar ao fim?
		if (aux != 0)
			return aux;
		else
			throw new RuntimeException();
	}
	
	// ##################################################################################
	static public boolean isSignedIn() {
		return (!Strings.isNullOrEmpty(mEmailAccount) ? true : false);
	}
	
	static public boolean isConnected() {
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
		Log.i(TAG, message);
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
		pantriesDbAdapter = new PantriesDBAdapter(EndPointCall.getGlobalContext());
		EndPointCall.SynchronizeAll();
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
	// TASK ##################################################################################
	static public void addToProductListTask(String name, Long barCode, String description, String image) {
		new AddToProductListTask(name, barCode, description, image).execute();
	}
	
	static public void addProductToPantryTask(MetaProductAdapter adapter, PantriesDBAdapter.PantryDB pantryDB, Long productBarCode) {
		ProductsDbAdapter productsDBAdapter = EndPointCall.getProductsDbAdapter();
		List<LocalProduct> ooo = productsDBAdapter.getProductByBarCode(productBarCode);
		if (productsDBAdapter.getAllProducts().isEmpty())
			throw new RuntimeException(); // FIXME LIXO REMOVE
		// ver na local BD
		if (ooo.isEmpty() && isConnected()) {
			// FIXME if not ver na AppEngine
		} else {
			// NONE ??!?!? FIXME
		}
		// if not perguntar se quer criar um, NAO QUER PORQUE EU NAO DEIXO, POR AGORA FIXME
		// if not exit FIXME
		if (ooo.isEmpty()) {
			msg(EndPointCall_TAG, "Unknown Product");
			/*
			 * FIXME AlertDialog.Builder alert = new AlertDialog.Builder(this); alert.setTitle("Pro"); alert.setItems(pantreisName, new DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which) { selectedPantryName = pantreisName[which];//FIXME final PantriesDbAdapter.PantryDB pantryDB = EndPointCall.getPantryDB(selectedPantryName); EndPointCall.listPantryProductTask(adapter, pantryDB); //FIXME } }); final Activity aux = this; alert.setOnCancelListener(new OnCancelListener() {
			 * 
			 * @Override public void onCancel(DialogInterface dialog) { aux.finish(); } }); alert.show();
			 */
		}
		else {
			if (ooo.size() == 1)
				new AddProductToLocalPantryTask(adapter, pantryDB, new LocalMetaProduct(ooo.get(0), 2.2)).execute();
			else
				new RuntimeException(); // FIXME
		}
		// new AddProductToPantryTask(adapter, selectedPantry, productId).execute(); // FIXME getSelectedPantry()
	}
	
	static public void listAllProductTask(ProductAdapter adapter) {
		new ListAllProductTask(adapter).execute();
	}
	
	static public void newPantryTask(String pantryName) {
		new NewPantryTask(pantryName).execute();
	}
	
	static public void listPantryProductTask(MetaProductAdapter adapter, PantriesDBAdapter.PantryDB pantryDB) {
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
		if (isConnected()) {
			new SynchronizePantryTask(pantryDB).execute();
		} else {
			msg(EndPointCall_TAG, "SynchronizePantry_FAIL: is not Connected"); // FIXME TEXT e ver estado
		}
	}
	
	static public void SynchronizeAll() {
		if (isConnected()) {
			new SynchronizeAll(EndPointCall.getEmailAccount()).execute();
		} else {
			msg(EndPointCall_TAG, "Synchronize_FAIL: is not Connected"); // FIXME TEXT e ver estado
		}
	}

	public static void plusOneOnProductAmoutTask(LocalMetaProduct product,PantryDB pantryDB) {
		new ChangeProductAmountTask(pantryDB).execute(product);
	}
}
