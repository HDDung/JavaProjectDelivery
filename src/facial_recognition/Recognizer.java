package facial_recognition;


import core.TrainingPacket;
import org.opencv.core.Mat;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import utils.Utils;


public class Recognizer {
	final private FaceRecognizer faceRecognizer;
	private boolean State = false;
	private TrainingPacket packet;
	private double[] confidence;
	private int[] labels;

	public Recognizer(){
		faceRecognizer = Face.createFisherFaceRecognizer();
		System.out.println("Recognizer created");
	}
	
	public void Training(TrainingPacket packet){
		if (State == false){
			this.packet = packet;
	        faceRecognizer.train(packet.getImages(), packet.getLabelsBuffer());
			confidence = new double[packet.getName().size()];
			labels = new int[packet.getName().size()];
			System.out.println("Training finish");
			System.out.println("Summary: #Name-" + packet.getName().size() +
					"#Faces-" + packet.getImages().size());
			State = true;
		}
	}

	public RecognizedFace Prediction(final Mat testImage) {

		final Mat temp = Utils.resizeFace(Utils.toGrayScale(testImage));
		System.out.println(faceRecognizer.predict(temp));
		faceRecognizer.predict(temp, labels, confidence);
		System.out.println(confidence[0] + "   " + labels[0]);
		if (confidence[0] < Constants.RECOGNITION_THRESHOLD && labels[0] != 0) {

			//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: " + Name.get(predictedLabel.intValue()));
			return new RecognizedFace(packet.getName().get(labels[0]), confidence[0]);
		}
		//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: Unknown");

		return new RecognizedFace("Unknown", 0.0);

	}
	
	

    
}
