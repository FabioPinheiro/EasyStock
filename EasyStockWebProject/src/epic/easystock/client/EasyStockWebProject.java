package epic.easystock.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import epic.easystock.client.service.LoginInfo;
import epic.easystock.client.service.LoginService;
import epic.easystock.client.service.LoginServiceAsync;
import epic.easystock.shared.FieldVerifier;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EasyStockWebProject implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Label logOutLabel = new Label("Please sign Out of the application.");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final Button logoutButton = new Button("Logout");
		//final TextBox nameField = new TextBox();
		//nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		sendButton.addStyleName("logoutButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("logoutButtonContainer").add(logoutButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Create the popup dialog box
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		
		final DialogBox logoutDialogBox = new DialogBox();
		logoutDialogBox.setText("Remote Procedure Call");
		logoutDialogBox.setAnimationEnabled(true);
		final Button logoutCloseButton = new Button("Close LogoutDialogBox");
		
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		
		logoutCloseButton.getElement().setId("closeButton");
		VerticalPanel logoutDialogVPanel = new VerticalPanel();;
		logoutDialogVPanel.add(new HTML("<b>Logout logoutDialogBox</b>"));
		logoutDialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		logoutDialogVPanel.add(logoutCloseButton);
		logoutDialogBox.setWidget(logoutDialogVPanel);
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});
		
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logoutDialogBox.hide();
				logoutButton.setEnabled(true);
				logoutButton.setFocus(true);
			}
		});
		
		class SendHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				sendHandlerMethod();
			}
			private void sendHandlerMethod(){
				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				
				// Check login status using login service.
				LoginServiceAsync loginService = GWT.create(LoginService.class);
				loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						//Show the RPC error message to the user
						System.out.print("onFailure(Throwable error \n");
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}
		 
					public void onSuccess(LoginInfo result) {
						System.out.print("onSuccess\n");
						loginInfo = result;
						if(loginInfo.isLoggedIn()) {
							dialogBox.setText("Remote Procedure Call");
							serverResponseLabel.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(result.getEmailAddress());//FIXME
							dialogBox.center();
							closeButton.setFocus(true);
						} else {
							// Assemble login panel.
							signInLink.setHref(loginInfo.getLoginUrl());
							loginPanel.add(loginLabel);
							loginPanel.add(signInLink);
							RootPanel.get("loginRoot").add(loginPanel);
						}
					}
				}); 
			}
		}
		
		class LogoutHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				logoutHandlerMethod();
			}
			private void logoutHandlerMethod(){
				signOutLink.setHref(loginInfo.getLogoutUrl());
				loginPanel.add(logOutLabel);
				loginPanel.add(signOutLink);
				RootPanel.get("loginRoot").add(loginPanel);
			}
		}
		

		// Add a handler to send the name to the server
		SendHandler sendHandler = new SendHandler();
		sendButton.addClickHandler(sendHandler);
		
		LogoutHandler logoutHandler = new LogoutHandler();
		logoutButton.addClickHandler(logoutHandler);
	}
}