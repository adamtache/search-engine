package view;

import controller.IController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import search.ISearchData;

public class MainScreen implements IScreen {

	private static final int HBOX_SPACING = 10;
	private static final int VBOX_SPACING = 20;
	private TextField textField;
	private WebEngine webEngine;
	private WebView webView;
	private Button searchButton;
	private Button feelingLuckyButton;
	private IController controller;
	private int myWidth;
	private int myHeight;
	private VBox myRoot;
	private Scene myScene;

	public MainScreen(IController controller, int windowWidth, int windowHeight) {
		this.controller = controller;
		this.webView = new WebView();
		this.webEngine = webView.getEngine();
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}

	@Override
	public void display(ISearchData data) {
		System.out.println("MainScreen printing data.");
		data.print();
	}

	@Override
	public void display(int result) {
		this.webEngine.load(controller.getResultUrl(result));
	}

	private void initialize() {
		myRoot = this.makeVbox();
		HBox hbox = this.makeHbox();
		this.textField = new TextField();
		setupSearchButton(searchButton);
		setupFeelingLuckyButton(feelingLuckyButton);
		hbox.getChildren().addAll(textField, searchButton, feelingLuckyButton);
		myRoot.getChildren().add(hbox);
		setupScrollPane();
		myRoot.getChildren().add(webView);
		myScene = new Scene(myRoot, myWidth, myHeight);
	}

	private void setupSearchButton(Button searchButton) {
		this.searchButton = new Button("Search");
		this.searchButton.setOnAction(event -> {
			System.out.println("Search button pressed.");
			controller.search(getSearchTerm());
			System.out.println("Finished search.");
			controller.display();
			System.out.println("Finished displaying. Done.");
		});
	}

	private void setupFeelingLuckyButton(Button feelingLuckyButton) {
		this.feelingLuckyButton = new Button("Feeling lucky?");
		this.feelingLuckyButton.setOnAction(event -> {
			controller.search(getSearchTerm());
			controller.display();
			controller.goTo(0);
		});
	}

	private void setupScrollPane() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(webView);
		scrollPane.getStyleClass().add("noborder-scroll-pane");
		scrollPane.setStyle("-fx-background-color: white");
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
	}

	private HBox makeHbox() {
		HBox hbox = new HBox(HBOX_SPACING);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}

	private VBox makeVbox() {
		VBox vbox = new VBox(VBOX_SPACING);
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}

	public String getSearchTerm() {
		return this.textField.getText();
	}

	public Scene getScene() {
		return myScene;
	}

}