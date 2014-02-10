package epic.easystock.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import epic.easystock.client.MethodsLib;
import epic.easystock.client.handler.ItemListHandler;

public class ItemsTable extends AbstractContent {
	ItemListHandler data = new ItemListHandler();	
	VerticalPanel verticalPanel= new VerticalPanel();
	private final Button reloadItemList = new Button("Reload Item List");
	public ItemsTable() {
		reloadItemList.addClickHandler(new ReloadItemListHandler());
		verticalPanel.add(reloadItemList);
		verticalPanel.add(data.getItemCellTable());
		initWidget(verticalPanel);
		
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		
		reloadModule();
	}

	@Override
	public void reloadModule() {
		super.reloadModule();

	}
	
	class ReloadItemListHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			MethodsLib.getItemsMethod(data);
		}
	}
}
