package epic.easystock.io;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.assist.AppConstants;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.assist.ProductAdapter;

public class EndPointCall {

	static private final String EndPointCall_TAG = "EndPointCall";
	
	private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222; //FIXME TODO
	
	static public final String FAIL_TO_LIST_PRODUCTS = "FAIL_TO_LIST_PRODUCTS";
	static public final String FAIL_TO_LIST_PANTRY_PRODUCTS = "FAIL_TO_LIST_PANTRY_PRODUCTS";
	static public final String DONE = "AsyncTask Done"; //FIXME remove!!! use in debug
	
	//static private ApiEndpoint.Builder apiEndpointBuilder = null;
	//static private ApiEndpoint apiEndpoint = null;
	static private Context globalContext = null;
	static private String mEmailAccount = null;
	//static private String selectedPantry = null;

	static public Context getGlobalContext() {return globalContext;}
	static public String getEmailAccount() {return mEmailAccount;}
	//static protected void setEmailAccount(String account) {mEmailAccount= account;}
	//public static String getSelectedPantry() {return selectedPantry;}
	//public static void setSelectedPantry(String selectedPantry) {EndPointCall.selectedPantry = selectedPantry;}
	static public boolean isSignedIn() {return (!Strings.isNullOrEmpty(mEmailAccount) ? true : false);}
	public static PantryDbAdapter getPantryDbAdapter() {return 	new PantryDbAdapter(EndPointCall.getGlobalContext());} //FIXME

	static public void msg(String message){
	Toast.makeText(EndPointCall.getGlobalContext(), message, Toast.LENGTH_LONG).show();
	}
	static public void msgNotSignedIn(){msg("Need To Sign In");} //FIXME TEXT
	static public void msgAddProductToPantry(){msg("Product add to Pantry");} //FIXME TEXT
		
	static public void onApplicationCreate(Context context){
		Log.i(EndPointCall_TAG, "onApplicationCreate");
		globalContext = context;
	}
	static public void onInit(Activity activity){
		Log.i(EndPointCall_TAG, "onInit()");
		Account aux = new AccountSelector(activity).selectorAccount(getGlobalContext());
		mEmailAccount = aux.name;
	}
	
	static public ApiEndpoint getApiEndpoint(){
		GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(EndPointCall.getGlobalContext(), AppConstants.AUDIENCE);
		credential.setSelectedAccountName(EndPointCall.getEmailAccount());
		ApiEndpoint endpoint = AppConstants.getApiServiceHandle(credential);
		return endpoint;
	}
	/*public static ApiEndpoint getApiEndpoint() { FIXME
		apiEndpointBuilder = new ApiEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {}
				});
		apiEndpoint = CloudEndpointUtils.updateBuilder(apiEndpointBuilder).build();
		return apiEndpoint;
	}*/

	// ##################################################################################
	static public void addToProductListTask(String name, Long barCode,String description, String image) {
		new AddToProductListTask(name, barCode, description, image).execute();
	}
	
	static public void addProductToPantryTask(MetaProductAdapter adapter, String selectedPantry, Long productId) {
		new AddProductToPantryTask(adapter, selectedPantry, productId).execute(); //FIXME getSelectedPantry()
		Log.i(EndPointCall_TAG, "addProductToPantryTask:" + "productId " + productId);
	}
	
	static public void listProductTask(ProductAdapter adapter){
		new ListAllProductTask(adapter).execute();
	}
	
	static public void listPantryProductTask(MetaProductAdapter adapter, String pantryID){
		if (isConnected()) {
			Log.i(EndPointCall_TAG, "isConnected()=" + isConnected());
			new ListPantryProductTask(adapter, pantryID).execute();
		} else {
			Log.i(EndPointCall_TAG, "isConnected()=" + isConnected());
			PantryDbAdapter bd = EndPointCall.getPantryDbAdapter();
			bd.open();
			adapter.addAll(bd.getAllProducts());
			adapter.notifyDataSetChanged();
			bd.close();
			//FIXME new LocalListPantryTask(adapter).execute(getApplicationContext());
		}
	}
	
	// ##################################################################################
	
	private static boolean isConnected() {
		boolean connected = false;
		ConnectivityManager conMan = (ConnectivityManager) EndPointCall.getGlobalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		// mobile
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ? null
				: conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
						.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null ? null
				: conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.getState();
		if (mobile != null
				&& (mobile == State.CONNECTED || mobile == State.CONNECTING)) {
			connected = true;
		}
		if (wifi != null
				&& (wifi == State.CONNECTED || wifi == State.CONNECTING)) {
			connected = true;
		}
		return connected;
	}
}
