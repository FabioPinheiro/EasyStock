package epic.easystock.activitys;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import epic.easystock.R;
import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.assist.EndpointCall;
import epic.easystock.assist.MetaProductAdapter;

public class PantyActivity extends ListActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_panty);
		MetaProductAdapter adapter = new MetaProductAdapter(this, new ArrayList<MetaProduct>());
		// 2. setListAdapter
		setListAdapter(adapter);
		EndpointCall.listPantryProductTask(adapter, 1l); //FIXME XXX 1l
	}

	
}
