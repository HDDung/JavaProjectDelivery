package facial_recognition;

import org.opencv.core.Size;

/**
 * Created by dung on 25.12.16.
 */
public class Constants {
    public static final double RECOGNITION_THRESHOLD = 700;
    public static int TRAIN_FACE_IMAGE_HEIGHT = 140;
    public static int TRAIN_FACE_IMAGE_WIDTH = TRAIN_FACE_IMAGE_HEIGHT;
    public static Size TRAIN_FACE_IMAGE_SIZE = new Size((double) TRAIN_FACE_IMAGE_HEIGHT, (double) TRAIN_FACE_IMAGE_HEIGHT);

}
