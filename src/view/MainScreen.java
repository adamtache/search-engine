package view;

import controller.Controller;

public class MainScreen extends Screen{

	
	public MainScreen(Controller controller, int windowWidth) {
		super(controller, windowWidth);
	}
	
	protected void setUpSearchBar(){
		this.mySearchBar = new MainSearchBar(myController, this);
	}
	
	@Override
	void setTabText() {
		this.myTab.setText("Search");
	}

}