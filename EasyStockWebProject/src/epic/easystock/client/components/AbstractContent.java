package epic.easystock.client.components;

import com.google.gwt.user.client.ui.Composite;

import epic.easystock.client.MethodsLib;

public abstract class AbstractContent extends Composite{
	public void loadModule(){ //super.loadModule();
		MethodsLib.log(this.getClass().getName(), "loadModule", "here");
	}
	public void reloadModule(){ //super.reloadModule();
		MethodsLib.log(this.getClass().getName(), "reloadModule", "here");
	}

}
