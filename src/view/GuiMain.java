package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuiMain extends Application{
		private static final int SCREEN_WIDTH = 1300;
		private static final int SCREEN_HEIGHT = 800;
		
		private Stage s;
		GUI myGui;
	    @Override
	    public void start(Stage myStage) throws Exception {
	    	s = new Stage();
	    	myGui = new GUI(SCREEN_WIDTH,SCREEN_HEIGHT);
	    	s.setScene(myGui.getScene());
	    	s.show();
	    }

	    public static void main(String[] args){
	        launch(args);
	    }
}
