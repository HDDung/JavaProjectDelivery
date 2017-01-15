package core;

import controller.CameraController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Mat;
import utils.Utils;

import java.util.Vector;

public class AlertBox{
	private Boolean State;
	private Integer counter;
	private ImageView imageView;

	public Integer displayImg(Vector<Mat> ListFace){


		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Face same Name");
		window.setMinHeight(200);

		Button NextButton = new Button("Next");
		Button YesButton = new Button("Yes");
		Button CancelButton = new Button("Cancel");
		counter = 0;
		Image imageToShow = Utils.mat2Image(ListFace.get(counter++));
		imageView = new ImageView();
		CameraController.updateImageView(imageView, imageToShow);

		NextButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if (counter < ListFace.size()) {
					Image imageToShow = Utils.mat2Image(ListFace.get(counter++));
					CameraController.updateImageView(imageView, imageToShow);
					System.out.println("Img Change");
				} else {
					State = false;
					window.close();
				}
		    }
		});
		
		YesButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
				State = true;
				window.close();
		    }	
		});
		
		CancelButton.setOnAction(e -> {
			State = null;
			window.close();
		});

		BorderPane layout = new BorderPane();
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(NextButton, YesButton, CancelButton);
		layout.setCenter(imageView);
		layout.setBottom(hbox);

		Scene scene = new Scene(layout);
	    window.setScene(scene);
	    window.setOnCloseRequest((new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we)
			{
				State = null;
				window.close();
			}
		}));
	    window.showAndWait();
	    if (!(State == null)){
			if (State.booleanValue() == true){
				return counter - 1;
			} else if (State.booleanValue() == false){
				return -1;
			}
		}

		return -2;

	}
}
