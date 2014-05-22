package epic.easystock.assist;

import java.util.ArrayList;

import com.google.api.client.util.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import epic.easystock.R;
import epic.easystock.data.LocalMetaProduct;

/**
 * @author fabio
 *
 */
public class MetaProductAdapter extends ArrayAdapter<LocalMetaProduct> {

	private final Context context;
	private final ArrayList<LocalMetaProduct> productArrayList;
	public MetaProductAdapter(Context context, ArrayList<LocalMetaProduct> itemsArrayList) {

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
		TextView labelViewAmount = (TextView) rowView.findViewById(R.id.product_row_label_amount);

		// 4. Set the text for textView
		//labelViewIdProduct.setText(productArrayList.get(position).getKey()/*getKey().getId()*/.toString());
		String name = productArrayList.get(position).getName();
		if(name.length()>20)
			name = name.substring(0, 20) + "...";
		labelViewName.setText(name);
		labelViewBarCode.setText(productArrayList.get(position).getBarCode().toString());
		//labelViewDescription.setText(productArrayList.get(position).getAmount().toString());
		labelViewAmount.setText(productArrayList.get(position).getAmount().toString());

		// 5. retrn rowView
		return rowView;
	}
}