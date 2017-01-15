package controller;

import application.Main;
import com.jfoenix.controls.JFXButton;
import core.DataHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Mat;
import utils.Console;
import utils.Utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a> (minor fixes)
 * @version 2.0 (2016-09-17)
 * @since 1.0 (2013-10-20)
 *
 */
public class FXController
{
	
	// a flag to change the button behavior
	private boolean IsLoadCameraScreen = false;
	// Child controller
	private CameraController camera_controller;
	// Stage of child
	private Stage camera_stage;
	private boolean holder = true;
	// Name of the file of face to be update
	private String Namefile;
	private Mat facewrite;

	private boolean AllowToNextImg = false;

	// the FXML button
		@FXML
		private JFXButton button,
			but_detect;
		// the FXML image view
		@FXML
		private ImageView currentFrame;
		@FXML
		private TextField Name;
	@FXML
	private TextArea console;

	private PrintStream ps;

	public void initialize() {
		ps = new PrintStream(new Console(console));
		System.setOut(ps);
		System.setErr(ps);
	}


	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 * //@param event
	 *            the push button event
	 * @throws IOException 
	 */
	
	private void LoadCameraScreen() throws IOException{
			try {
				System.out.println("Open new screen");
				FXMLLoader loader_camera = new FXMLLoader(getClass().getResource("/fxml/Camera_Screen.fxml"));
				Parent root1 = loader_camera.load();
				camera_stage = new Stage();
				camera_stage.setTitle("Camera");
				camera_stage.setScene(new Scene(root1, 600, 400));
				camera_stage.show();
				camera_controller = new CameraController();
				camera_controller = loader_camera.getController();
				camera_stage.setResizable(false);


				//set the proper behavior on closing the camera_screen
				camera_stage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we)
					{
						camera_controller.setClosed();
						IsLoadCameraScreen = false;
						button.setText("Start Camera");
						but_detect.setText("Start detection");
						
					}
				}));
				
			} catch (Exception e){
				System.err.println("Cannot open Camera Screen");
			}
		
		
	}
	
	
	@FXML
	protected void startCamera(ActionEvent event) 
	{
		// if Camera Screen not load
		if (!IsLoadCameraScreen){
			try {
				this.LoadCameraScreen();
				this.camera_controller.startCamera();
				this.IsLoadCameraScreen = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else // Change title of button
			if (!this.camera_controller.IsCameraRun()){
			this.camera_controller.startCamera();

			} else {
				this.camera_controller.startCamera();
			}
		if (this.camera_controller.IsCameraRun()) {
			this.button.setText("Stop Camera");
		} else {
			this.button.setText("Start Camera");
		}
			
			
	}

	 
	@FXML 
	protected void CaptureFace(ActionEvent event) throws InterruptedException{
		if (this.camera_controller != null && this.camera_controller.IsActivateDetector()) {
			System.out.println("Capture call");
			Name.clear();
			final Vector<Mat> unknownFaces = this.camera_controller.UnknownFaces();
			if (!unknownFaces.isEmpty()) {
				Runnable unknown = () -> {
					for (Mat face : unknownFaces) {
						AllowToNextImg = true;
						facewrite = face;
						Image imageToShow = Utils.mat2Image(face);
						CameraController.updateImageView(currentFrame, imageToShow);
						while (holder) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								Thread.currentThread().interrupt();
							}

						}
						// update face to database

						holder = true;
					}
					AllowToNextImg = false;
				};

				ExecutorService run = Executors.newSingleThreadExecutor();
				run.submit(unknown);
				holder = true;
				run.shutdownNow();

			} else {
				System.err.println("No unknown face");
			}
		} else {
			System.err.println("Need to open detector");
		}
	}

	@FXML
	protected void StartDetection(ActionEvent event){
		if (IsLoadCameraScreen) {
			if (!this.camera_controller.IsActivateDetector()) {
				this.camera_controller.ActivateDetector();
				this.but_detect.setText("Stop detection");
			} else {
				this.camera_controller.DeactivateDetector();
				this.but_detect.setText("Start detection");
			} 
		} else {
			System.err.println("Camera didn't open");
		}
	}
	
	@FXML
	protected void MoveNextImg(ActionEvent event){
		if (AllowToNextImg) {
			System.out.println(Name.getText());
			if (!Name.getText().trim().equals("")) {
				Namefile = Main.manager.IntegrityName(Name.getText());
				holder = false;
				if (Namefile != null) {
					DataHandler.updateData(Namefile, facewrite, Main.manager.getSize());
				}
				Namefile = new String();
				Main.manager.updateDatabase();
			} else {
				System.err.print("Please insert Name");
			}
			Name.clear();
		} else {
			System.err.println("No more unknown face");
			Name.clear();
		}
	}
	/**
	 * On application close, stop the acquisition from the camera
	 */
	public void setClosed()
	{
		if (IsLoadCameraScreen) {
			this.camera_controller.setClosed();
			this.camera_stage.close();
		}

	}


	
	
	
	
}