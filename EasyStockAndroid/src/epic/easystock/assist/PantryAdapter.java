package epic.easystock.assist;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import epic.easystock.R;
import epic.easystock.activitys.PantrySettingActivity;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.data.UserBdAdapter.UserPantryAux;
import epic.easystock.io.EndPointCall;

public class PantryAdapter extends ArrayAdapter<UserPantryAux> {

	private Context context;
	private List<UserPantryAux> productArrayList;

	public PantryAdapter(Context context, List<UserPantryAux> objects) {
		super(context, R.layout.layout_list_pantry_row, objects);

		this.context = context;
		this.productArrayList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.layout_list_pantry_row,
				parent, false);

		// 3. Get the two text view from the rowView
		TextView labelViewIdProduct = (TextView) rowView
				.findViewById(R.id.pantry_row_label_idpantry);
		TextView labelViewName = (TextView) rowView
				.findViewById(R.id.pantry_row_label_name);
		ImageButton button = (ImageButton) rowView.findViewById(R.id.imageButton1);
		
		
		
		final String pantryName = productArrayList.get(position).pantryName;
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EndPointCall.setmCurrentPantry(pantryName);
				Intent intent = new Intent(EndPointCall.getGlobalContext(),PantrySettingActivity.class);
				context.startActivity(intent);
			}
		});
		
		// 4. Set the text for textView
		labelViewIdProduct.setText(String.valueOf(productArrayList.get(position).pantryID));
		labelViewName.setText(pantryName);


		// 5. retrn rowView
		return rowView;
	}

}
