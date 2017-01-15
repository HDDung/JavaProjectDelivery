package core;

import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;

public class TrainingPacket {
	private List<Mat> images;
	private HashMap<Integer, String> Name = new HashMap<>();

	private MatOfInt labelsBuffer;
	private Size StandardImgSize;
	
	
	public TrainingPacket(List<Mat> images, MatOfInt labelsBuffer, Size StandardImgSize, HashMap<Integer, String> Name){
		this.images = images;
		this.labelsBuffer = labelsBuffer;
		this.StandardImgSize = StandardImgSize;
		this.Name = Name;
	}
	
	/**
	 * @return the images
	 */
	public List<Mat> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<Mat> images) {
		this.images = images;
	}
	/**
	 * @return the labelsBuffer
	 */
	public MatOfInt getLabelsBuffer() {
		return labelsBuffer;
	}
	/**
	 * @param labelsBuffer the labelsBuffer to set
	 */
	public void setLabelsBuffer(MatOfInt labelsBuffer) {
		this.labelsBuffer = labelsBuffer;
	}
	/**
	 * @return the standardImgSize
	 */
	public Size getStandardImgSize() {
		return StandardImgSize;
	}
	/**
	 * @param standardImgSize the standardImgSize to set
	 */
	public void setStandardImgSize(Size standardImgSize) {
		StandardImgSize = standardImgSize;
	}

	/**
	 * @return the name
	 */
	public HashMap<Integer, String> getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(HashMap<Integer, String> name) {
		Name = name;
	} 
	
	
}
