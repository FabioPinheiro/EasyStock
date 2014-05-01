package epic.easystock.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.Strings;

import epic.easystock.apiEndpoint.model.Product;

public class AddToProductListTask extends AsyncTask<Void, Integer, Void> {
	static private final String LOG_TAG = AuthorizationCheckTask.class
			.getCanonicalName();
	static private final String FAIL_TO_ADD_PRODUCT = "FAIL_TO_ADD_PRODUCT"; // FIXME RES
	// Context context;
	// Intent intent;
	// String mail;

	String name;
	Long barCode;
	String description;
	String image;

	public AddToProductListTask(String name, Long barCode, String description,
			String image) {
		super();
		this.name = name;
		this.barCode = barCode;
		this.description = description;
		this.image = image;
	}

	@Override
	protected Void doInBackground(Void... v) {
		Log.i(LOG_TAG, "Background task started.");
		String emailAccount = EndPointCall.getEmailAccount();

		if (Strings.isNullOrEmpty(emailAccount)) {
			Log.e(LOG_TAG, "isNullOrEmpty(emailAccount)=" + emailAccount);
			return null;// Void
		}
		try {
			// Create a Google credential since this is an authenticated
			// request
			// to the API.

			Product content = new Product();
			content.setName(this.name);
			content.setBarCode(this.barCode);
			content.setDescription(this.description);
			if (!Strings.isNullOrEmpty(this.image)) {
				Bitmap bmp = BitmapFactory.decodeFile(this.image);
				if (bmp != null && bmp.getByteCount() > 5) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					content.encodeImage(byteArray);
				}
			}
			Product result = EndPointCall.getApiEndpoint()
					.insertProduct(content).execute();
			Log.i(LOG_TAG, "Done" + result.toString());
		} catch (IOException e) {
			Log.e(LOG_TAG, FAIL_TO_ADD_PRODUCT);
			e.printStackTrace();
		}
		return null;
	}
}