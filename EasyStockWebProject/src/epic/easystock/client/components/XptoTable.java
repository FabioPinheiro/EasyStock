package epic.easystock.client.components;

import com.google.gwt.user.client.ui.FlexTable;

public class XptoTable extends AbstractContent {

	private FlexTable flexTable = new FlexTable();

	public XptoTable() {
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		flexTable.addStyleName("FlexTable");
		initWidget(flexTable);
		reloadModule();
	}

	@Override
	public void reloadModule() {
		super.reloadModule();
		flexTable.setText(0, 0, "0,0");
		flexTable.setText(0, 1, "0,1");
		flexTable.setText(1, 0, "1,0");
		flexTable.setText(1, 1, "1,1");
		flexTable.setText(2, 0, "2,0");
		flexTable.setText(2, 1, "2,1");
	}
}
