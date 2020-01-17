package com.faces.recognition.resource;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.faces.recognition.DTO.MessageDTO;
import com.faces.recognition.model.Person;
import com.faces.recognition.service.InsertFacesService;
import com.faces.recognition.service.PersonService;
import com.faces.recognition.service.RecognitionFacesService;

@RestController
@RequestMapping("/recognition/")
public class RecognitionResource {

	@Autowired
	private InsertFacesService insertFacesService;
	@Autowired
	private PersonService personService;
	@Autowired
	private RecognitionFacesService recognitionService;

	
	@PostMapping("/insert/pictures/person-name")
	public void insertPicturesAndPerson(@RequestParam("name")  String name, @RequestParam("file") MultipartFile file) {
		
		try {
			Person person = personService.insert(new Person(name, file.getBytes()));
			insertFacesService.insert(person, file.getBytes());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/verify/multipart")
	public MessageDTO fileUpload(@RequestParam("file") MultipartFile file) {
		try {
			return recognitionService.recognize(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}