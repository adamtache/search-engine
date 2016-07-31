package view;

import controller.IController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ButtonSetup {

	public static EventHandler<ActionEvent> searchButtonAction(String term, IController controller) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Search button pressed.");
				controller.search(term);
				System.out.println("Finished search.");
				controller.display();
				System.out.println("Finished displaying. Done.");
			}
		};
	}

	public static EventHandler<ActionEvent> feelingLuckyAction(String term, IController controller){
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.search(term);
				controller.display();
				controller.go_to(0);
			}
		};
	}
	
}