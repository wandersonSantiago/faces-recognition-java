/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.faces.recognition.service;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.springframework.stereotype.Service;

@Service
public class TreiningFacesService {

	public void treinar() {
		File diretorio = new File("src/main/resources/pictures/");
		FilenameFilter filtroImagem = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String nome) {
				return nome.endsWith(".jpg") || nome.endsWith(".gif") || nome.endsWith(".png");
			}
		};

		File[] arquivos = diretorio.listFiles(filtroImagem);
		MatVector fotos = new MatVector(arquivos.length);
		Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
		IntBuffer rotulosBuffer = rotulos.createBuffer();
		int count = 0;
		for (File imagem : arquivos) {
			Mat foto = imread(imagem.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
			String f = imagem.getName().substring(0, imagem.getName().lastIndexOf("_"));
			int classe = Integer.parseInt(f);

			resize(foto, foto, new Size(160, 160));
			fotos.put(count, foto);
			rotulosBuffer.put(count, classe);
			count++;
		}

		FaceRecognizer lbph = createLBPHFaceRecognizer(2, 8, 8, 8, 1);

		lbph.train(fotos, rotulos);
		lbph.save("src/main/resources/classifiers/classificadorLBPH.yml");

	}

}
