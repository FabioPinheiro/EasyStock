package epic.easystock.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import epic.easystock.client.EasyStockWebProject;
import epic.easystock.client.MethodsLib;

public class Lobby extends AbstractContent {

	private HorizontalPanel panel = new HorizontalPanel();
	private LogInHandler logInHandler = new LogInHandler();
	private LogoutHandler logoutHandler = new LogoutHandler();
	private final Button logInButton = new Button("Log In");
	private final Button logOutButton = new Button("Log Out");
	private Label emailLabel = new Label("Email:not loaded");
	private Label nickNameLabel = new Label("NickName:not loaded");

	public Lobby() {
		loadModule();
	}
	
	@Override
	public void loadModule() {
		super.loadModule();
		logInButton.addClickHandler(logInHandler);
		logOutButton.addClickHandler(logoutHandler);
		panel.add(logInButton);
		panel.add(logOutButton);
		panel.add(emailLabel);
		panel.add(nickNameLabel);
		initWidget(panel);
		reloadModule();
	}

	@Override
	public void reloadModule() {
		super.reloadModule();
		if(EasyStockWebProject.getLoginInfo()== null ||
		   !EasyStockWebProject.getLoginInfo().isLoggedIn()){
			logInButton.setVisible(true);
			logOutButton.setVisible(false);
			emailLabel.setText("Email:none");
			nickNameLabel.setText("NickName:none");
		}else{
			logInButton.setVisible(false);
			logOutButton.setVisible(true);
			emailLabel.setText(EasyStockWebProject.getLoginInfo().getEmailAddress());
			nickNameLabel.setText(EasyStockWebProject.getLoginInfo().getNickname());
		}
	}
	
	class LogInHandler implements ClickHandler {
		public void onClick1(ClickEvent event) {
			MethodsLib.logInMethod();
		}
		@Override
		public void onClick(ClickEvent event) {
			MethodsLib.logInMethod();
		}
	}
	
	class LogoutHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			MethodsLib.logOutMethod();
		}

	}
}
