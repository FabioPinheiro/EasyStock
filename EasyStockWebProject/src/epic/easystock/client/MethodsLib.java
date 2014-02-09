package epic.easystock.client;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epic.easystock.client.service.LoginInfo;
import epic.easystock.client.service.LoginService;
import epic.easystock.client.service.LoginServiceAsync;

public class MethodsLib {
	static LinkedList<String> methodsLibLog = new LinkedList<String>();
	
	/*private static void log(String message){
		methodsLibLog.add(message);
		GWT.log(message);
	}*/
	
	public static void log(String methodName, String message){
		methodsLibLog.add(MethodsLib.class.getName() +" - " + methodName + ": " + message);
		GWT.log(MethodsLib.class.getName() +" - " + methodName + ": " + message);
	}
	
	public static void log(String className, String methodName, String message){
		methodsLibLog.add(className +" - " + methodName + ": " + message);
		GWT.log(className +" - " + methodName + ": " + message);
	}
	
	public static void logInMethod(){
		// Check login status using login service.
		LoginServiceAsync service = GWT.create(LoginService.class);
		service.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				//FIXME Show the RPC error message to the user
				log("logInMethod", "onFailure -> Remote Procedure Call - Failure");
			}
			
			public void onSuccess(LoginInfo result) {
				EasyStockWebProject.setLoginInfo(result);
				log("logInMethod","onSuccess -> Remote Procedure Call");
				if(result.isLoggedIn()) {
					//emailLabel.setText(result.getEmailAddress());
				} else {
					Window.Location.assign(result.getLoginUrl());
				}
				EasyStockWebProject.contentContainer.lobby.reloadModule();
			}
		}); 
	}
	
	public static void saveSarviceTestMethod(String name, String type){
		// Check login status using login service.
		LoginServiceAsync service = GWT.create(LoginService.class);
		service.saveItemService(name, type, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				//FIXME Show the RPC error message to the user
				log("saveSarviceTestMethod", "onFailure -> Remote Procedure Call - Failure");
			}
			
			public void onSuccess(Void result) {
				log("saveSarviceTestMethod","onSuccess -> Remote Procedure Call");
			}
		});
	}
	
	public static void logOutMethod(){
		Window.Location.assign(EasyStockWebProject.getLoginInfo().getLogoutUrl());
	}
}
