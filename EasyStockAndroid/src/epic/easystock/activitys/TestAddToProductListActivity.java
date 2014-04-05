package epic.easystock.activitys;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

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
import epic.easystock.assist.EndpointCall;

public class TestAddToProductListActivity extends Activity {

	private OnTouchListener addListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_add_to_product_list);
		takePic = (Button) findViewById(epic.easystock.R.id.take_pic_button);
		imageView = (ImageView) findViewById(epic.easystock.R.id.add_prod_pic);
	}

	Button takePic;
	ImageView imageView;

	static final int REQUEST_IMAGE_CAPTURE = 1;

	public void takePic(View view) {
		dispatchTakePictureIntent();
	}

	String mCurrentPhotoPath;

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
		Toast.makeText(this, "setPic called!",
				Toast.LENGTH_SHORT).show();
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
	}

	public void addXPTO(View view) {
		// new EndpointsTask().execute(getApplicationContext());
		String name = ((EditText) findViewById(R.id.activity_test_add_to_product_list_name))
				.getText().toString();
		Long barCode = Long
				.parseLong(((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode))
						.getText().toString());
		String description = ((EditText) findViewById(R.id.activity_test_add_to_product_list_description))
				.getText().toString();
		
		EndpointCall.addProductTask(name, barCode, description);
	}

	/*
	 * FIXME LIXO public class EndpointsTask extends AsyncTask<Context, Integer,
	 * Long> {
	 * 
	 * @Override protected Long doInBackground(Context... contexts) {
	 * Productendpoint.Builder endpointBuilder = new Productendpoint.Builder(
	 * AndroidHttp.newCompatibleTransport(), new JacksonFactory(), new
	 * HttpRequestInitializer() { public void initialize(HttpRequest
	 * httpRequest) { } }); Productendpoint endpoint =
	 * CloudEndpointUtils.updateBuilder( endpointBuilder).build(); try { String
	 * name = ((EditText)
	 * findViewById(R.id.activity_test_add_to_product_list_name
	 * )).getText().toString(); Long barCode = Long.parseLong(((EditText)
	 * findViewById
	 * (R.id.activity_test_add_to_product_list_barCode)).getText().toString());
	 * String description = ((EditText)
	 * findViewById(R.id.activity_test_add_to_product_list_description
	 * )).getText().toString();
	 * 
	 * //Product productAux = new Product(idProduct, name, barCode,
	 * description);
	 * 
	 * epic.easystock.productendpoint.model.Product content = new
	 * epic.easystock.productendpoint.model.Product();
	 * 
	 * content.setName(name); content.setBarCode(barCode);
	 * content.setDescription(description);
	 * 
	 * epic.easystock.productendpoint.model.Product result =
	 * endpoint.insertProduct(content).execute(); } catch (IOException e) {
	 * e.printStackTrace(); } return (long) 0; } }
	 */
}
