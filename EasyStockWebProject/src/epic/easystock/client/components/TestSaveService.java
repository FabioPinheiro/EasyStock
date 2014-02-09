package epic.easystock.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import epic.easystock.client.EasyStockWebProject;
import epic.easystock.client.MethodsLib;

public class TestSaveService extends AbstractContent {

	private HorizontalPanel panel = new HorizontalPanel();
	private SaveServiceTestHandler saveServiceTestHandler = new SaveServiceTestHandler();
	private final Button saveServiceTestButton = new Button("Save");
	private TextBox nomeTextBox = new TextBox();
	private TextBox typeTextBox = new TextBox();
	private Label saveServiceTestLabel = new Label("New Item:");

	public TestSaveService() {
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		saveServiceTestButton.addClickHandler(saveServiceTestHandler);
		nomeTextBox.setText("nome");
		typeTextBox.setText("type");
		
		panel.add(saveServiceTestLabel);
		panel.add(nomeTextBox);
		panel.add(typeTextBox);
		panel.add(saveServiceTestButton);
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
			MethodsLib.saveSarviceTestMethod(nomeTextBox.getText(), typeTextBox.getText());
		}
	}
}
