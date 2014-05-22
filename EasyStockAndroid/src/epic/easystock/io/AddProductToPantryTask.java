package epic.easystock.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.assist.MetaProductAdapter;
import epic.easystock.data.LocalMetaProduct;

//FIXME isto tem muito problemasde syscronização(falar com ofabio)
public class AddProductToPantryTask extends AsyncTask<Void, Integer, LocalMetaProduct> {
	private final String LOG_TAG = this.getClass().getCanonicalName();

	private MetaProductAdapter adapter;
	private String mail, pantrySelected;
	private long productId;

	public AddProductToPantryTask(MetaProductAdapter adapter, String pantrySelected, long productId) {
		this.adapter = adapter;
		this.mail = EndPointCall.getEmailAccount();
		this.pantrySelected = pantrySelected;
		this.productId = productId;
		
	}

	@Override
	protected LocalMetaProduct doInBackground(Void... v) {
		try {
			Pantry pantry = EndPointCall.getApiEndpoint().getPantryByMailAndName(mail, pantrySelected).execute();
			List<MetaProduct> newList = pantry.getProducts();
			if (newList == null) {
				Log.e(LOG_TAG, "AddProductTask: newList=null");
				newList = new ArrayList<MetaProduct>(); // FIXME isto nunca devia de ser null
			}
			MetaProduct metaP = null;
			Product newProd = null;
			boolean exist = false;
			for (MetaProduct mp : newList) {
				newProd = EndPointCall.getApiEndpoint().getProductByBarCode(productId).execute();
				if (mp.getProduct().equals(newProd.getKey())) {
					newList.remove(mp);
					exist = true;
					metaP = mp;
					mp.setAmount(mp.getAmount() + 1);
					Log.i(LOG_TAG, "AddProductTask: Product was in the list");
					break;
				}
			}
			if (!exist) {
				metaP = new MetaProduct();
				newProd = EndPointCall.getApiEndpoint().getProductByBarCode(productId).execute();
				metaP.setProduct(newProd.getKey());
				metaP.setAmount(0.0);
				newList.add(metaP);
			}
			pantry.setProducts(newList);
			Log.i(LOG_TAG, "AddProductTask:" + "Product added to pantry");
			throw new RuntimeException();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(LOG_TAG, "AddProductTask:" + "Product NOT added to pantry", e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(LocalMetaProduct result) {
		super.onPostExecute(result);
		adapter.add(result);
		EndPointCall.msgAddProductToPantry();
	}
}
