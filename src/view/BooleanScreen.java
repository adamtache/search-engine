package view;

import controller.Controller;

public class BooleanScreen extends Screen{

	public BooleanScreen(Controller controller, int windowWidth) {
		super(controller, windowWidth);
	}

	@Override
	protected void setUpSearchBar(){
		this.mySearchBar = new MainSearchBar(myController, this);
		this.mySearchBar.searchButton.setText("Boolean Search");
	}

	@Override
	void setTabText() {
		this.myTab.setText("Boolean Search");
	}

	
}
