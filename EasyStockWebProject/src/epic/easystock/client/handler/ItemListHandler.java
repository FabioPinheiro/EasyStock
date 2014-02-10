package epic.easystock.client.handler;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

import epic.easystock.shared.Item;

public class ItemListHandler{
	CellTable<Item> itemCellTable = new CellTable<Item>();
	public final CellTable<Item> getItemCellTable() {return itemCellTable;}

	ListDataProvider<Item> dataProvider = new ListDataProvider<Item>();
	
	TextColumn<Item> nameColumn = 	new TextColumn<Item>() {@Override public String getValue(Item item) {return item.getName();}};
	TextColumn<Item> emailColumn = 	new TextColumn<Item>() {@Override public String getValue(Item item) {return item.getEmail();}};
	TextColumn<Item> typeColumn = 	new TextColumn<Item>() {@Override public String getValue(Item item) {return item.getType();}};
	TextColumn<Item> amountColumn =	new TextColumn<Item>() {@Override public String getValue(Item item) {return item.getAmount().toString();}};
	
	public ItemListHandler(){
		// Add the columns.
		itemCellTable.addColumn(nameColumn, "Name");
		itemCellTable.addColumn(emailColumn, "Email");
		itemCellTable.addColumn(typeColumn, "Type");
		itemCellTable.addColumn(amountColumn, "Amount");
		
		// Connect the table to the data provider.
		dataProvider.addDataDisplay(itemCellTable);
	}
	
	public void handler(List<Item> Items) {
		// Add the data to the data provider, which automatically pushes it to the
		// widget.
		List<Item> list = dataProvider.getList();
		list.clear();
		for (Item it : Items) {
			list.add(it);
		}
	}
	
	/*
	// Make the name column sortable.
	nameColumn.setSortable(true);
	// Add a ColumnSortEvent.ListHandler to connect sorting to the
	// java.util.List.
	ListHandler<Item> columnSortHandler = new ListHandler<ItemsTable.Item>(list);
	columnSortHandler.setComparator(nameColumn,new Comparator<ItemsTable.Item>() {
		public int compare(Item o1, Item o2) {
			if (o1 == o2) {
				return 0;
			}
			// Compare the name columns.
			if (o1 != null) {
				return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
			}
			return -1;
		}
	});
	itemCellTable.addColumnSortHandler(columnSortHandler);
	
	// We know that the data is sorted alphabetically by default.
	itemCellTable.getColumnSortList().push(nameColumn);*/

}
