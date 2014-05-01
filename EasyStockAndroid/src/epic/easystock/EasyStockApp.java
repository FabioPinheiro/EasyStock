package epic.easystock;

import epic.easystock.assist.endPointCall.EndPointCall;
import android.app.Application;
import android.util.Log;

public class EasyStockApp extends Application {
	static private final String APP_TAG = "EasyStockApp";
	
	public void onCreate() {
		super.onCreate();
		Log.i(APP_TAG, "onCreate()");
		EndPointCall.onApplicationCreate(this.getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(APP_TAG, "onTerminate()");
	}

}
