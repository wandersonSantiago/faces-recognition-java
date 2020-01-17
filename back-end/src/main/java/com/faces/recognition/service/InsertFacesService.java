
package com.faces.recognition.service;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.faces.recognition.model.Person;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class InsertFacesService {

	@Autowired
	private TreiningFacesService treiningService;
	
	private CascadeClassifier classifier;
	
	private String time = "" + new Date().getTime();


	public void insert(Person person, byte[] bt) throws InterruptedException, IOException {

		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();

		classifier = new CascadeClassifier(
				"src/main/resources/classifiers/haarcascade_frontalface_alt.xml");

		Java2DFrameConverter f = new Java2DFrameConverter();
		ByteArrayInputStream bis = new ByteArrayInputStream(bt);
		BufferedImage bImage = ImageIO.read(bis);
		
		Mat colorPicture = converteMat.convert(f.convert(bImage));
		Mat grayImage = new Mat();
		cvtColor(colorPicture, grayImage, COLOR_BGRA2GRAY);
		RectVector facesDetectadas = new RectVector();
		resize(grayImage, grayImage, new Size(250, 250));
		
		classifier.detectMultiScale(grayImage, facesDetectadas, 1.1, 2, 0, new Size(100, 100), new Size(500, 500));
		
		for (int i = 0; i < facesDetectadas.size(); i++) {
			Rect dadosFace = facesDetectadas.get(i);
			rectangle(colorPicture, dadosFace, new Scalar(0, 0, 255, 0));
			Mat faceCapturada = new Mat(grayImage, dadosFace);
			resize(faceCapturada, faceCapturada, new Size(160, 160));
			imwrite("src/main/resources/pictures/" + person.getId() + "_" + time + ".jpg", faceCapturada);
		}
		treiningService.treinar();
	}
}
