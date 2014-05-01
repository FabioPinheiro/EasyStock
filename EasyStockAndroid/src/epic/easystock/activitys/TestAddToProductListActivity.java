package epic.easystock.activitys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import epic.easystock.R;
import epic.easystock.io.EndPointCall;

public class TestAddToProductListActivity extends Activity {

	//LIXO private OnTouchListener addListener = null;
	private static String mail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
		takePic = (Button) findViewById(R.id.take_pic_button);
		imageView = (ImageView) findViewById(R.id.add_prod_pic);
		mail = EndPointCall.getEmailAccount(); // FIXME LIXO
												// getIntent().getStringExtra("MAIL");
	}

	Button takePic;
	ImageView imageView;

	static final int REQUEST_IMAGE_CAPTURE = 1;

	public void takePic(View view) {
		dispatchTakePictureIntent();
	}

	static String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
		// Create an image file name
		Toast.makeText(this, "createImageFile called!", Toast.LENGTH_SHORT)
				.show();

		String timeStamp = SimpleDateFormat.getDateTimeInstance().format(
				new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_SHORT)
				.show();
		Toast.makeText(this, mCurrentPhotoPath + " Cenas", Toast.LENGTH_SHORT)
				.show();

		return image;
	}

	static final int REQUEST_TAKE_PHOTO = 1;

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go

			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				Toast.makeText(this,
						"IOException catched! \n " + ex.getLocalizedMessage(),
						Toast.LENGTH_SHORT).show();
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {

				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			} else
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			setPic();
		}
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();
		Toast.makeText(this, "setPic called!", Toast.LENGTH_SHORT).show();
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		imageView.setImageBitmap(bitmap);

		Toast.makeText(this, mail, Toast.LENGTH_SHORT).show();

	}

	public void addXPTO(View view) {
		// new EndpointsTask().execute(getApplicationContext());
		String name = ((EditText) findViewById(R.id.activity_test_add_to_product_list_name)).getText().toString();
		Long barCode = Long.parseLong(((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode)).getText().toString());
		String description = ((EditText) findViewById(R.id.activity_test_add_to_product_list_description)).getText().toString();
		EndPointCall.addToProductListTask(name, barCode, description,mCurrentPhotoPath);
	}

	/*
	 * LIXO public static class EndpointsTask extends
	 * AsyncTask<epic.easystock.activitys
	 * .TestAddToProductListActivity.EndpointsTask.Param, Integer, Long> {
	 * public static class Param { String name; Long barCode; String
	 * description; String image;
	 * 
	 * public Param(String name, Long barCode, String description, String image)
	 * { super(); this.name = name; this.barCode = barCode; this.description =
	 * description; this.image = image; } }
	 * 
	 * Context context; Intent intent; String mail;
	 * 
	 * public EndpointsTask(Context incontext, Intent intent, String mail) {
	 * context = incontext; this.mail = mail; this.intent = intent; }
	 * 
	 * @Override protected Long doInBackground(
	 * epic.easystock.activitys.TestAddToProductListActivity
	 * .EndpointsTask.Param... params) { try { // Product productAux = new
	 * Product(idProduct, name, barCode, // description); if (!isSignedIn()) {
	 * Log.i("ADD PRODUCT", "NOT SIGNED IN"); return null; }
	 * 
	 * // Create a Google credential since this is an authenticated // request
	 * // to the API. GoogleAccountCredential credential =
	 * GoogleAccountCredential .usingAudience(context, AppConstants.AUDIENCE);
	 * credential.setSelectedAccountName(mail); ApiEndpoint endpoint =
	 * AppConstants .getApiServiceHandle(credential);// FIXME
	 * 
	 * Product content = new Product(); content.setName(params[0].name);
	 * content.setBarCode(params[0].barCode);
	 * content.setDescription(params[0].description); if
	 * (!Strings.isNullOrEmpty(params[0].image)) { Bitmap bmp =
	 * BitmapFactory.decodeFile(params[0].image); if (bmp != null &&
	 * bmp.getByteCount() > 5) { ByteArrayOutputStream stream = new
	 * ByteArrayOutputStream(); bmp.compress(Bitmap.CompressFormat.PNG, 100,
	 * stream); byte[] byteArray = stream.toByteArray();
	 * content.encodeImage(byteArray); } } Log.i("ADD PRODUCT", "TEST"); Product
	 * result = endpoint.insertProduct(content).execute(); } catch (IOException
	 * e) { e.printStackTrace(); } return (long) 0; // FIXME } }
	 */
}
