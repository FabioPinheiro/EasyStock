package epic.easystock.io;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.User;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.assist.ProductAdapter;

public class EndPointCall {
	static private final String EndPointCall_TAG = "EndPointCall";
	// static private final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;
	// //FIXME TODO
	static private final String PREFS_FILE_NAME = "EasyStockPrefsFile";
	static private final String PREFS_FIRST_INIT = "PREFS_FIRST_INIT";
	static private final String PREFS_LAST_USED_EMAIL = "PREFS_LAST_USED_EMAIL";
	static private final String PREFS_LAST_USED_PANTRY = "PREFS_LAST_USED_PANTRY";
	static private final String ERROR_PANTRY_NAME = "ERROR_PANTRY_NAME";
	static public final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static public final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static public final String FAIL_TO_LOAD_PANTRY = "FAIL_TO_LOAD_PANTRY";
	static public final String FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME = "FAIL_TO_CREATE_PANTRY_WITHOUT_A_NAME";
	static public final String FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER = "FAIL_TO_CREATE_PANTRY_WITH_THE_NAME_OF_ANOTHER";
	static public final String INSERT_NEW_USER_IN_APPENGINE = "INSERT_NEW_USER_IN_APPENGINE";
	static public final String DONE = "AsyncTask Done"; // FIXME remove!!! use
	static public final String DEBUG = "DEBUG";
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
	public static void setSelectedPantry(String selectedPantry) {
		globalSettings.edit().putString(PREFS_LAST_USED_PANTRY, selectedPantry).commit();
	}
	public static String getSelectedPantry() {
		return globalSettings.getString(PREFS_LAST_USED_PANTRY, ERROR_PANTRY_NAME);
	}
	// ##################################################################################
	private static boolean isConnected() {
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
	public static PantryDbAdapter getPantryDbAdapter() {
		String name = getSelectedPantry();
		if (name == ERROR_PANTRY_NAME) {
			msg(ERROR_PANTRY_NAME);
			Log.e(EndPointCall_TAG, ERROR_PANTRY_NAME);
			throw new UnsupportedOperationException();
		}
		return new PantryDbAdapter(EndPointCall.getGlobalContext(), name);
	}
	public static UserBdAdapter getUserDbAdapter() {
		UserBdAdapter aux = new UserBdAdapter(EndPointCall.getGlobalContext());
		aux.open(); //FIXME falta close .... e esta sepre a abri
		return aux;
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
		if (fistTime) {
			globalSettings.edit().putBoolean(PREFS_FIRST_INIT, false).commit();
			Account aux = new AccountSelector(getGlobalContext()).selectorAccount();
			globalSettings.edit().putString(PREFS_LAST_USED_EMAIL, aux.name).commit();// FIXME
			mEmailAccount = aux.name;
			msg("Hello " + mEmailAccount);
			EndPointCall.getUserDbAdapter().createPantry(mEmailAccount, "pantry1");// FIXME
			EndPointCall.getUserDbAdapter().createPantry(mEmailAccount, "pantry2");// FIXME
		} else {
			mEmailAccount = globalSettings.getString(PREFS_LAST_USED_EMAIL, "EMAIL_ERROR");
			msg("Welcome Back " + mEmailAccount);// FIXME TEXT
		}
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
	static public void addProductToPantryTask(MetaProductAdapter adapter, String selectedPantry, Long productId) {
		new AddProductToPantryTask(adapter, selectedPantry, productId).execute(); // FIXME getSelectedPantry()
		Log.i(EndPointCall_TAG, "addProductToPantryTask:" + "productId " + productId);
	}
	static public void listProductTask(ProductAdapter adapter) {
		new ListAllProductTask(adapter).execute();
	}
	static public void listPantryProductTask(MetaProductAdapter adapter, String pantryID) {
		Log.i(EndPointCall_TAG, "isConnected()=" + isConnected());
		if (isConnected()) {
			new ListPantryProductTask(adapter, pantryID).execute();
		} else {
			PantryDbAdapter bd = EndPointCall.getPantryDbAdapter();
			bd.open();
			adapter.addAll(bd.getAllProducts());
			adapter.notifyDataSetChanged();
			bd.close();
			// throw new UnsupportedOperationException();
			// FIXME new ListPantrysTask(adapter).execute();
		}
	}
}
