package epic.easystock.assist;

import java.io.IOException;
import java.util.ArrayList;

import epic.easystock.R;
import epic.easystock.apiEndpoint.ApiEndpoint;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Product;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author fabio
 *
 */
public class MetaProductAdapter extends ArrayAdapter<MetaProduct> {

	private final Context context;
	private final ArrayList<MetaProduct> productArrayList;
	public MetaProductAdapter(Context context, ArrayList<MetaProduct> itemsArrayList) {

		super(context, R.layout.layout_list_product_row, itemsArrayList);

		this.context = context;
		this.productArrayList = itemsArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.layout_list_product_row, parent, false);

		// 3. Get the two text view from the rowView
		TextView labelViewIdProduct = (TextView) rowView.findViewById(R.id.product_row_label_idProduct);
		TextView labelViewName = (TextView) rowView.findViewById(R.id.product_row_label_name);
		TextView labelViewBarCode = (TextView) rowView.findViewById(R.id.product_row_label_barCode);
		TextView labelViewDescription = (TextView) rowView.findViewById(R.id.product_row_label_description);
//		TextView labelViewAmount = (TextView) rowView.findViewById(R.id.product_row_label_Amount);

		// 4. Set the text for textView
		ApiEndpoint endpoint = AppConstants.getApiServiceHandle(null);
		Product product = null;
		try {
			product = endpoint.getProduct(productArrayList.get(position).getProduct()).execute();
		} catch (IOException e) {
			e.printStackTrace();
			return rowView;
		}
		labelViewIdProduct.setText(productArrayList.get(position).getKey().getId().toString());
		labelViewName.setText(product.getName());
		labelViewBarCode.setText(product.getBarCode().toString());
		labelViewDescription.setText(productArrayList.get(position).getAmount().toString());
//		labelViewAmount.setText(productArrayList.get(position).getAmount());

		// 5. retrn rowView
		return rowView;
	}
}