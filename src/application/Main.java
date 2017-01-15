package application;

import controller.FXController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

public class Main extends Application {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ControlPanel.fxml"));
			// store the root element so that the controllers can use it
			AnchorPane rootElement =  loader.load();
			// create and style a scene
			Scene scene = new Scene(rootElement, 600, 400);


			primaryStage.setTitle("Control panel");
			primaryStage.setScene(scene);

			// show the GUI
			primaryStage.show();

			// set the proper behavior on closing the application
			FXController controller = loader.getController();
			controller.initialize();
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we)
				{
					controller.setClosed();
				}
			}));


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
