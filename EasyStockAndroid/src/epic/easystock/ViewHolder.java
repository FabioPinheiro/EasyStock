package epic.easystock;

import android.view.View;
import android.widget.TextView;

public class ViewHolder {
	private View row;
	private TextView upperText = null, lowerText = null;

	public void ListWidgetWrapper(View row) {
		this.row = row;
	}

	public TextView getUpperText() {
		if (this.upperText == null) {
			//this.upperText = (TextView) row.findViewById(R.id.label);
		}
		return this.upperText;
	}

	public TextView getLowerText() {
		if (this.lowerText == null) {
			//this.lowerText = (TextView) row.findViewById(R.id.value);
		}
		return this.lowerText;
	}
}