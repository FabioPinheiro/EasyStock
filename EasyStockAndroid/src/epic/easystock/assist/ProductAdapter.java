package epic.easystock.assist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import epic.easystock.R;
import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.data.LocalMetaProduct;
import epic.easystock.data.LocalProduct;

/**
 * @author fabio
 *
 */
public class ProductAdapter extends ArrayAdapter<LocalProduct> {

	private final Context context;
	private final ArrayList<LocalProduct> productArrayList;
	
	public ProductAdapter(Context context, ArrayList<LocalProduct> itemsArrayList) {
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
		//TextView labelViewIdProduct = (TextView) rowView.findViewById(R.id.product_row_label_idProduct);
		TextView labelViewName = (TextView) rowView.findViewById(R.id.product_row_label_name);
		TextView labelViewBarCode = (TextView) rowView.findViewById(R.id.product_row_label_barCode);
		//TextView labelViewDescription = (TextView) rowView.findViewById(R.id.product_row_label_description);

		// 4. Set the text for textView
		//labelViewIdProduct.setText(productArrayList.get(position).getKey().toString());
		labelViewName.setText(productArrayList.get(position).getName());
		labelViewBarCode.setText(productArrayList.get(position).getBarCode().toString());
		//labelViewDescription.setText(productArrayList.get(position).getDescription());

		// 5. retrn rowView
		return rowView;
	}
}