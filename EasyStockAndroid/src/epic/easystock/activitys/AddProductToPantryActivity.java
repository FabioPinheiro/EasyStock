package epic.easystock.activitys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import epic.easystock.R;
import epic.easystock.R.id;
import epic.easystock.R.layout;
import epic.easystock.R.menu;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.data.PantriesDBAdapter.PantryDB;
import epic.easystock.io.EndPointCall;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class AddProductToPantryActivity extends Activity {
	// LIXO private OnTouchListener addListener = null;
		private static String mail;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_product_to_pantry);
			takePic = (Button) findViewById(R.id.take_pic_button);
			imageView = (ImageView) findViewById(R.id.add_prod_pic);
			mail = EndPointCall.getEmailAccount(); // FIXME LIXO
													// getIntent().getStringExtra("MAIL");
			pantryName = getIntent().getStringExtra("PANTRYNAME");
		}

		Button takePic;
		ImageView imageView;
		private MetaProductAdapter adapter;
		private String pantryName;
		private PantryDB pantryDB = EndPointCall.getPantryDB(pantryName);

		static final int REQUEST_IMAGE_CAPTURE = 1;

		public void takePic(View view) {
			// dispatchTakePictureIntent();
			dispatchBarcode();

		}

		private void dispatchBarcode() {
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
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
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {

			/*
			 * protected void onActivityResult(int requestCode, int resultCode,
			 * Intent data) { if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode
			 * == RESULT_OK) { setPic(); }
			 */

			// retrieve scan result
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);

			if (scanningResult != null) {
				// we have a result
				String scanContent = scanningResult.getContents();
				String scanFormat = scanningResult.getFormatName();
				((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode))
						.setText(scanContent);
				/*
				 * formatTxt.setText("FORMAT: " + scanFormat);
				 * contentTxt.setText("CONTENT: " + scanContent);
				 */
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No scan data received!", Toast.LENGTH_SHORT);
				toast.show();
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
			String name = ((EditText) findViewById(R.id.activity_test_add_to_product_list_name))
					.getText().toString();
			Long barCode = Long
					.parseLong(((EditText) findViewById(R.id.activity_test_add_to_product_list_barCode))
							.getText().toString());
			String description = ((EditText) findViewById(R.id.activity_test_add_to_product_list_description))
					.getText().toString();
			
			Product result = new Product();
			result.setBarCode(barCode);
			result.setDescription(description);
			result.setName(name);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("PRODNAME", name);
			returnIntent.putExtra("PRODDESCRIPTION", description);
			returnIntent.putExtra("PRODBARCODE", barCode);
			setResult(RESULT_OK, returnIntent);
			finish();
			
//			EndPointCall.addProductToPantryTask(adapter, pantryDB, barCode);
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.add_product_to_pantry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
