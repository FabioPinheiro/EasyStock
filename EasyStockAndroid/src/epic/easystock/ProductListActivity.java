package epic.easystock;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProductListActivity extends Activity {
	
	private View viewContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		ListView l = (ListView) findViewById(R.id.listview_product_list);
		//FIXME l.childDrawableStateChanged(findViewById(R.id.xpto));
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		//l.inflate(this, findViewById(R.id.xpto), findViewById(R.id.xpto));
		viewContainer = findViewById(R.id.undobar);
		l.setAdapter(adapter);
	}

	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showUndo(viewContainer);
		return true;
	}

	public void onClick(View view) {
		Toast.makeText(this, "Deletion undone", Toast.LENGTH_LONG).show();
		viewContainer.setVisibility(View.GONE);
	}

	public static void showUndo(final View viewContainer) {
		viewContainer.setVisibility(View.VISIBLE);
		viewContainer.setAlpha(1);
		viewContainer.animate().alpha(0.4f).setDuration(5000)
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						viewContainer.setVisibility(View.GONE);
					}
				});

	}
/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_product_list);
	 * 
	 * final ListView listview = (ListView)
	 * findViewById(R.id.listview_activity_product); String[] values = new
	 * String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
	 * "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7",
	 * "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
	 * "OS/2", "Android", "iPhone", "WindowsMobile" };
	 * 
	 * final ArrayList<String> list = new ArrayList<String>(); for (int i = 0; i
	 * < values.length; ++i) { list.add(values[i]); } final StableArrayAdapter
	 * adapter = new StableArrayAdapter(this,
	 * android.R.layout.simple_list_item_1, list); listview.setAdapter(adapter);
	 * 
	 * listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, final View view,
	 * int position, long id) { final String item = (String)
	 * parent.getItemAtPosition(position);
	 * view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
	 * 
	 * @Override public void run() { list.remove(item);
	 * adapter.notifyDataSetChanged(); view.setAlpha(1); } }); }
	 * 
	 * }); }
	 * 
	 * private class StableArrayAdapter extends ArrayAdapter<String> {
	 * 
	 * HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	 * 
	 * public StableArrayAdapter(Context context, int textViewResourceId,
	 * List<String> objects) { super(context, textViewResourceId, objects); for
	 * (int i = 0; i < objects.size(); ++i) { mIdMap.put(objects.get(i), i); } }
	 * 
	 * @Override public long getItemId(int position) { String item =
	 * getItem(position); return mIdMap.get(item); }
	 * 
	 * @Override public boolean hasStableIds() { return true; }
	 * 
	 * }
	 */
}
