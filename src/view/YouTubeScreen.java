package view;

import controller.Controller;

public class YouTubeScreen extends Screen{

	public YouTubeScreen(Controller controller, int windowWidth) {
		super(controller, windowWidth);
	}

	@Override
	protected void setUpSearchBar(){
		this.mySearchBar = new YoutubeSearchBar(myController, this);
	}

	@Override
	protected void setTabText() {
		this.myTab.setText("YouTube Search");
	}

}
