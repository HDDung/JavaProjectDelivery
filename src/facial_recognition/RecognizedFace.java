package facial_recognition;


/**
 * Created by dung on 25.12.16.
 * Source:  andreaiacono Github
 * (https://github.com/andreaiacono/OpenCVDemo/blob/master/src/main/java/org/opencv/demo/core/RecognizedFace.java)
 */
public class RecognizedFace {
    private final String name;
    private final Double confidence;

    public RecognizedFace(String name, Double confidence) {
        this.confidence = ((Constants.RECOGNITION_THRESHOLD - confidence) / Constants.RECOGNITION_THRESHOLD) * 100;
        this.name = name;
    }

    public Double getConfidence() {
        return confidence;
    }

    public String getName() {
        return name;
    }

}
