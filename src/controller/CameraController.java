package controller;

import application.Main;
import core.Supervisor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import utils.Utils;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CameraController
{
    // the id of the camera to be used
    private static int cameraId = 0;
    @FXML
    private ImageView currentFrame;
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// a flag to activate the detector
	private boolean IsActivateDetector = false;
	// Detector for Facial detection


	public static void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}
	
    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
	 * @param \\view  the {@link ImageView} to update
	 * @param \\image the {@link Image} to show
	 */

	public void startCamera()
	{
		if (!this.cameraActive) // if program not run, it run, if already run, it go to else block
		{
			// start the video capture
			this.capture.open(cameraId);

            // is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

                    @Override
					public void run()
					{
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						if (IsActivateDetector){
							Image imageToShow = Utils.mat2Image(Main.manager.ImgProcessor(frame));
							CameraController.updateImageView(currentFrame, imageToShow);

						} else {
							Image imageToShow = Utils.mat2Image(frame);
							CameraController.updateImageView(currentFrame, imageToShow);
						}

                    }
				};

                try {
                    this.timer = Executors.newSingleThreadScheduledExecutor();
                    this.timer.scheduleWithFixedDelay(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				} catch (Exception e){
					System.err.println("Cannot create timer");
				}
				// update the button content

            } else {
                // log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content

            // stop the timer
			this.stopAcquisition();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame()
	{
		// init everything
		Mat frame = new Mat();

        // check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);

            }
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

        return frame;
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition()
	{
		if (this.timer!=null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(100, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

        if (this.capture.isOpened())
		{
			// release the camera
			this.capture.release();
		}
	}

	/**
	 * On application close, stop the acquisition from the camera
	 */
	public void setClosed()
	{
		this.stopAcquisition();
	}

	public void ActivateDetector() {
		Main.CreatNewSuppervisor();
		IsActivateDetector = true;
	}
/*
	public Supervisor Manager() {
		return manager;
	}*/

	public void DeactivateDetector() {
		IsActivateDetector = false;
	}

	public boolean IsActivateDetector() {
		return IsActivateDetector;
	}

	public boolean IsCameraRun() {
		return cameraActive;
	}

	public Vector<Mat> UnknownFaces() {
		return Main.manager.UnknownFaces(grabFrame());
	}
}
