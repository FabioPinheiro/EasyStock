package epic.easystock;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import epic.easystock.io.EndPointCall;

public class EasyStockApp extends Application {
	static private final String APP_TAG = "EasyStockApp";
	
	public void onCreate() {
		super.onCreate();
		Log.i(APP_TAG, "onCreate()");
		Context context = this.getApplicationContext();
		EndPointCall.onApplicationCreate(context);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(APP_TAG, "onTerminate()");
	}

}
