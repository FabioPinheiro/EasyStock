package epic.easystock.client;

import com.google.gwt.user.client.ui.RootPanel;

import epic.easystock.client.components.AbstractContent;
import epic.easystock.client.components.Lobby;
import epic.easystock.client.components.XptoTable;
import epic.easystock.client.service.LoginInfo;

public class PageConductor {
	public Lobby lobby = null;
	public XptoTable xptoTable = null;
	
	public void loadPageConductor() {
		MethodsLib.log(this.getClass().getName().toString(), "loadPageConductor", "load all");
		//FIXME RootPanel.get("header").add(new Header());
		//FIXME RootPanel.get("leftnav").add(new NavigationMenu());
		//FIXME RootPanel.get("footer").add(new Footer());
		//ContentContainer.setContent(new Lobby());
		this.setContentLobby();
		this.setContentXptoTable();
	}
	
	/*public void setContent(AbstractContent content) {
		RootPanel.get("content").add(content);
	}*/
	
	public void setContentLobby() {
		if(lobby == null){ lobby = new Lobby();}
		RootPanel.get("header").add(lobby);
	}
	
	public void setContentXptoTable() {
		if(xptoTable == null){ xptoTable = new XptoTable();}
		RootPanel.get("content").add(xptoTable);
	}
}
