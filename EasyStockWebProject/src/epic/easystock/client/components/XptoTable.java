package epic.easystock.client.components;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class XptoTable extends AbstractContent {

	Grid grid = new Grid(3, 2);
	FileUpload upload = new FileUpload();
	TextBox textBox = new TextBox();
    Label textLabel = new Label("Name");
    Label fileLabel = new Label("Upload Something");
    Button submit = new Button("Submit");
    
    
	public XptoTable() {
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		textBox.setName("textBox");
		initWidget(grid);
		/*cenas*/
		reloadModule();
	}

	@Override
	public void reloadModule() {
		super.reloadModule();
		grid.setWidget(0, 0, textLabel);
        grid.setWidget(0, 1, textBox);
        
        upload.setName("upload");
        grid.setWidget(1, 0, fileLabel);
        grid.setWidget(1, 1, upload);
        
        grid.setWidget(2, 0, submit);

    }
}
