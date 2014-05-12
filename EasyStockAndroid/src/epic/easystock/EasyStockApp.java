package epic.easystock;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import epic.easystock.io.EndPointCall;

public class EasyStockApp extends Application {
	static private final String APP_TAG = "EasyStockApp";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(APP_TAG, "onCreate()");
		Context context = this.getApplicationContext();
		EndPointCall.onApplicationCreate(context);
	}

	@Override 	//This method is for use in emulated process environments. It will never be called on a production Android device, where processes are removed by simply killing them; no user code (including this callback) is executed when doing so.
	public void onTerminate() {
		super.onTerminate();
		Log.i(APP_TAG, "onTerminate()");
	}

}
