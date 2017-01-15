package core;

import facial_detection.Detector;
import facial_recognition.RecognizedFace;
import facial_recognition.Recognizer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Vector;

public class Supervisor {
	private final String trainingDir = "Face_Recog/B";
	private Detector detector;
	private Recognizer recognizer;
	private DataHandler handler;
	private Vector<Mat> unknownFaces;
	
	public Supervisor(){
		this.detector = new Detector();
		this.recognizer = new Recognizer();
		this.handler = new DataHandler();
		handler.getData(trainingDir);
		recognizer.Training(handler.getTrainingPacket());
	}

	public Mat ImgProcessor(Mat frame) {
		MatOfRect faces = detector.detectAndDisplay(frame);

		Vector<RecognizedFace> name = PredictionFace(faces, frame);

		return detector.DrawnFace(faces, frame, name);
	}

	private Vector<RecognizedFace> PredictionFace(MatOfRect faces, Mat frame) {
		System.out.println(recognizer.Prediction(Imgcodecs.imread("Face_Recog/B/3-Anh-1-.bmp")).getName());
		Vector<RecognizedFace> result = new Vector<>();
		for (Rect face : faces.toArray()){
			result.addElement(recognizer.Prediction(new Mat(frame, face)));
		}
		return result;
	}
	
	public String IntegrityName(String name){
		String result;
		if (handler.IsNameIn(name)){
			Integer ID;
			ID = ShowImg(handler.ListID(name));
			if (ID != null) {
				if (ID.intValue() >= 0) {
					result = trainingDir + "/" + ID + "-" + name + "-" + new Integer(handler.NumOfIn(ID) + 1).toString() + "-.bmp";
				} else if (ID.intValue() == -1) {
					result = trainingDir + "/" + new Integer(handler.LatestLabel() + 1).toString() + "-" + name + "-1-.bmp";
				} else {
					result = null;
				}
			} else {
				result = null;
			}

		} else {
			//index-name-1-.bmp
			result = trainingDir + "/" + new Integer(handler.LatestLabel() + 1).toString() + "-" + name + "-1-.bmp";
		}
		System.out.println(result);
		return result;
	}

	private Integer ShowImg(Vector<Integer> ListID) {

		Vector<Mat> ListFace = new Vector<>();

		for (int i = 0; i < ListID.size(); i++) {
			ListFace.add(handler.GetFaceOf(ListID.get(i)));
		}

		AlertBox alertBox = new AlertBox();
		Integer num = alertBox.displayImg(ListFace);

		if (num.equals(-1)) {
			System.out.println("Not the same face in database");
			return new Integer(-1);
		} else if (num.equals(-2)) {
			System.out.print("error occur");
			return null;
		} else {
			System.out.println("ID_return is " + ListID.get(num).toString());
			return ListID.get(num);
		}
	}
	// can be change to static
	public Vector<Mat> UnknownFaces(Mat frame){
		unknownFaces = new Vector<>();
		MatOfRect faces = detector.detectAndDisplay(frame);
		Vector<RecognizedFace> name = PredictionFace(faces, frame);
		
		int count = 0;
		for (Rect face : faces.toArray()){
			if (name.get(count).getName().equals("Unknown")) {
				unknownFaces.addElement(new Mat(frame, face ));
			}
		}
		return this.unknownFaces;
	}

	public void updateDatabase() {
		handler.getData(trainingDir);
	}

	public Size getSize() {
		return handler.getStandardImgSize();
	}
}
