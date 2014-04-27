package epic.easystock.assist.endPointCall;

import java.io.IOException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Strings;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import epic.easystock.CloudEndpointUtils;
import epic.easystock.R;
import epic.easystock.activitys.HomeActivity;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.assist.ProductAdapter;

public class EndPointCall {
	static private final String EndPointCall_TAG = "EndPointCall";
	
	private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222; //Para é que isto serve?
	
	static private final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static private final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static private final String FAIL_TO_ADD_PRODUCT = "FAIL_TO_ADD_PRODUCT";
	static private final String DONE = "AsyncTask Done"; //FIXME remove!!! use in debug
	
	static private ApiEndpoint.Builder apiEndpointBuilder = null;
	static private ApiEndpoint apiEndpoint = null;
	static private Context globalContext = null;
	static private String mEmailAccount = null;

	
	static public String getEmailAccount() {return mEmailAccount;}
	static public boolean isSignedIn() {return (!Strings.isNullOrEmpty(mEmailAccount) ? true : false);}

	
	static public void onCreate(Activity activity){
		globalContext = activity.getApplicationContext();
		Log.i(EndPointCall_TAG, "On init()");
		
		
		int googleAccounts = AppConstants.countGoogleAccounts(globalContext);
		if (googleAccounts == 0) {
			// No accounts registered, nothing to do.
			Log.e(EndPointCall_TAG, "No google Accounts Registered");//RES move to String XML
			msg("No google Accounts Registered");
		} else if (googleAccounts == 1) {
			// If only one account then select it.
			AccountManager am = AccountManager.get(globalContext);
			Account[] accounts = am
					.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			if (accounts != null && accounts.length > 0) {
				// Select account and perform authorization check.
				mEmailAccount = accounts[0].name;
				AuthorizationCheckTask.performAuthCheck(activity, new String[] {mEmailAccount});
			}
		} else {
			// More than one Google Account is present, a chooser is necessary.

			// Reset selected account.

			// Invoke an {@code Intent} to allow the user to select a Google
			// account.
			Intent accountSelector = AccountPicker.newChooseAccountIntent(null,
					null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE },
					false, "Select the account to access the EasyStock API.",
					null, null, null);
			activity.startActivityForResult(accountSelector,ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION); //FIXME TEST
		}
		//Intent intent = new Intent(this, HomeActivity.class);
		//intent.putExtra("MAIL", mEmailAccount);
		//startActivity(intent);
		
		apiEndpointBuilder = new ApiEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		apiEndpoint = CloudEndpointUtils.updateBuilder(
				apiEndpointBuilder).build();
	}
	
	public static ApiEndpoint getApiEndpoint() {
		return apiEndpoint;
	}
	public static Context getGlobalContext() {
		return globalContext;
	}
	static private void msg(String message){
		Toast.makeText(EndPointCall.getGlobalContext(), message, Toast.LENGTH_LONG).show();
	}
	static public void addProductTask(String name, Long barCode,
			String description) {
		//new AddProductTask().execute(new epic.easystock.assist.EndpointCall.AddProductTask.Param(name, barCode, description));
	}
	
	static public void listProductTask(ProductAdapter adapter){
		//new ListAllProductTask(adapter).execute();
	}
	
	static public void listPantryProductTask(MetaProductAdapter adapter, Long pantryID){
		//new ListPantryProductTask(adapter, pantryID).execute();
	}
	
		// ##################################################################################
}
