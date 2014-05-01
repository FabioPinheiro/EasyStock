package epic.easystock.io;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

public class AccountSelector {
	static private final String TAG = AccountSelector.class.getCanonicalName(); // FIXME
	private Context context;
	private AccountManager accountManager;
	private Account[] accounts;
	
	public int countGoogleAccounts() { return (accounts == null || accounts.length < 1) ? 0 : accounts.length;}

	AccountSelector(Context context){
		this.context = context;
		this.accountManager = AccountManager.get(context);
		this.accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
	}
	
	public Account selectorAccount(Context context) {
		Account aux;
		if (countGoogleAccounts() == 0) {
				// No accounts registered, nothing to do.
				EndPointCall.msg("No google Accounts Registered");
				Log.e(TAG, "No google Accounts Registered");
				return null;
		}else if (countGoogleAccounts() == 1) {
			// If only one account then select it.
			if (accounts != null && accounts.length > 0) {
				// Select account and perform authorization check.
				aux = accounts[0];
			} else {
				EndPointCall.msg(TAG+ " selectAccount error: accounts");
				Log.e(TAG, "selectAccount error: accounts=" + accounts);
				return null;
			}
		} else {
			EndPointCall.msg(TAG+ " selectAccount error: googleAccounts>1");
			Log.e(TAG, "selectAccount error: googleAccounts>1 accounts=" + accounts); //FIXME TODO
			aux = accounts[0]; //FIXME TODO
				// More than one Google Account is present, a chooser is necessary.

				// Reset selected account.

				// Invoke an {@code Intent} to allow the user to select a Google
				// account.
				/*Intent accountSelector = AccountPicker.newChooseAccountIntent(null,
						null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE },
						false, "Select the account to access the EasyStock API.",
						null, null, null);
				activity.startActivityForResult(accountSelector, ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);*/ // FIXME TEST
		}
		AuthorizationCheckTask.performAuthCheck(context,new String[] { aux.name });
		EndPointCall.msg("Selected Account: " + aux.name);
		return aux;
	}
}
