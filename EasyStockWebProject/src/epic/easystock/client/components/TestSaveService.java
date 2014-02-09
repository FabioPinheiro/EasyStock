package epic.easystock.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import epic.easystock.client.EasyStockWebProject;
import epic.easystock.client.MethodsLib;

public class TestSaveService extends AbstractContent {

	private HorizontalPanel panel = new HorizontalPanel();
	private SaveServiceTestHandler saveServiceTestHandler = new SaveServiceTestHandler();
	private final Button saveServiceTestButton = new Button("saveServiceTestButton");
	private Label saveServiceTestLabel = new Label("saveServiceTestLabel");

	public TestSaveService() {
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		saveServiceTestButton.addClickHandler(saveServiceTestHandler);
		panel.add(saveServiceTestButton);
		panel.add(saveServiceTestLabel);
		initWidget(panel);
		reloadModule();
	}

	@Override
	public void reloadModule() {
		super.reloadModule();
	}
	
	class SaveServiceTestHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			MethodsLib.saveSarviceTestMethod();
		}
	}
}
