package com.faces.recognition.service;

import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faces.recognition.DTO.MessageDTO;
import com.faces.recognition.model.Person;
import com.faces.recognition.repository.PersonRepository;

@Service
public class RecognitionFacesService {

	private CascadeClassifier detectorFace;
	private Mat imagemColorida;

	@Autowired
	private PersonRepository personRepository;

	public MessageDTO recognize(byte[] bt) throws IOException {

		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();

		Java2DFrameConverter f = new Java2DFrameConverter();

		ByteArrayInputStream bis = new ByteArrayInputStream(bt);
		BufferedImage bImage = ImageIO.read(bis);

		imagemColorida = new Mat();

		imagemColorida = converteMat.convert(f.convert(bImage));
		Mat imagemCinza = new Mat();
		cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
		RectVector facesDetectadas = new RectVector();
		
		detectorFace = new CascadeClassifier("src/main/resources/classifiers/haarcascade_frontalface_alt.xml");
		detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 2, 0, new Size(100, 100), new Size(500, 500));

		if (facesDetectadas.size() == 0) {
			return new MessageDTO("Undetected face", null);
		}

		FaceRecognizer reconhecedor = createLBPHFaceRecognizer();
		reconhecedor.load("src/main/resources/classifiers/classificadorLBPH.yml");

		for (int i = 0; i < facesDetectadas.size(); i++) {
			Rect dadosFace = facesDetectadas.get(i);
			rectangle(imagemColorida, dadosFace, new Scalar(0, 255, 0, 0));
			Mat faceCapturada = new Mat(imagemCinza, dadosFace);
			resize(faceCapturada, faceCapturada, new Size(160, 160));

			IntPointer rotulo = new IntPointer(1);
			DoublePointer confianca = new DoublePointer(1);
			reconhecedor.predict(faceCapturada, rotulo, confianca);
			Integer predicao = rotulo.get(0);
			if(confianca.get(0) > 150.0) {
				return new MessageDTO("==> Indice de confiança muito abaixo do esperado  ==>" + confianca.get(0), null);
			}
			if (predicao != -1) {
				Person person = personRepository.findById(predicao).orElse(null);
				if(person != null) {
					return new MessageDTO("==> Indice de confiança ==>" + confianca.get(0), person);
				}
			}
		}
		return new MessageDTO("Pessoa não encontrada", null);

	}
}
