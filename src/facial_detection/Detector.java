package facial_detection;

import facial_recognition.RecognizedFace;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

public class Detector {
	
	private String face_cascade_name = "HaarCascade/haarcascade_frontalface_alt_test.xml";
	private CascadeClassifier face_cascade = new CascadeClassifier(); 


	public Detector(){
		try 
		{
			this.face_cascade.load( face_cascade_name );
			System.out.println("Finish creating detector");
		} catch (CvException e){
			System.err.print("Error loading face cascade");
		}
	}
	
	public MatOfRect detectAndDisplay(Mat frame){
		
		/*
		 * follow the tutorial from opencv-java-tutorials
		 * 
		 * */
		Mat grayFrame = new Mat();
		int absoluteFaceSize = 0;
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (20% of the frame height, in our case)
		if (absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				absoluteFaceSize = Math.round(height * 0.2f);
			}
		}
		
		MatOfRect faces = new MatOfRect();
		
		// detect faces
		this.face_cascade.detectMultiScale(grayFrame, faces, 1.1, 2,
				0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size());
		
	    return faces;
		
	}

	public Mat DrawnFace(MatOfRect ListFaces, Mat frame, Vector<RecognizedFace> name) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		int count = 0;
		for (Rect face : ListFaces.toArray()){
			String text = name.elementAt(count).getName() + " - " +
					df.format(name.elementAt(count).getConfidence()).toString() + "%";
			count++;
			// draw rectangle
			Imgproc.rectangle(frame, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height),
								new Scalar(0, 255, 0));

			Imgproc.putText(frame, text, new Point(face.x, face.y), Core.FONT_HERSHEY_TRIPLEX, 1,
					new Scalar(0, 255, 0), 2);
		}

		return frame;
		
	}
	
}
