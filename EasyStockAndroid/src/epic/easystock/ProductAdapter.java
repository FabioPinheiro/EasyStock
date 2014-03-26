package epic.easystock;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {

	private ArrayList<String> comments;
	Context mContext;

	public void CustomAdapter(Context context, ArrayList<String> comments) {
		this.mContext = context;
		this.comments = comments;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		String item = comments.get(position);

		if (getItemViewType(position) == 0) {

			View v = convertView;
			if (v == null) {

				// GET View 1
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.fragment_example, null);

				v = viewGroup;
			}

			// Fill Data for Ist view
			TextView comm = (TextView) v.findViewById(R.id.button1);
			comm.setText(item);

			return v;

		} else if (getItemViewType(position) == 1) {

			View v = convertView;
			if (v == null) {

				// GET View 2
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.fragment_example, null);

				v = viewGroup;
			}

			// Fill Data for IInd view
			TextView comm = (TextView) v.findViewById(R.id.button1);
			comm.setText(item);

			return v;

		} else {

			// GET View 3
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.fragment_example, null);

				v = viewGroup;
			}

			// Fill Data for IInd view
			TextView comm = (TextView) v.findViewById(R.id.button1);
			comm.setText(item);

			return v;

		}

	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		if (position == 0)
			return 0;
		else if (position == 1)
			return 1;
		else
			return 2;
	}
}