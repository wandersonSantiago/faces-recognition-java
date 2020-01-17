package com.faces.recognition;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.faces.recognition.model.Person;
import com.faces.recognition.service.InsertFacesService;
import com.faces.recognition.service.PersonService;

@SpringBootTest
public class treiningFacesTest {

	@Value("${path.photos}")
	private String path;
	@Autowired
	private InsertFacesService insertFacesService;
	@Autowired
	private PersonService personService;

	@Test
	public void initTest() throws IOException, InterruptedException {

		Random gerador = new Random();

		File file = new File(path);
		File[] arquivos = file.listFiles();
		

		for (File item : arquivos) {
			Person person = insertPerson(gerador.nextInt(3989), item.getName());
			insertFacesService.insert(person, encodeFileToBase64Binary(item.getName()));			
		}
	}

	public Person insertPerson(Integer id, String name) {
		Person person = new Person();
		person.setId(id);
		person.setName(name);
		personService.insert(person);
		return person;
	}
	
	private byte[] encodeFileToBase64Binary(String filePath) throws IOException {
		return  FileUtils.readFileToByteArray(new File(path + "/" +filePath));
	}

}
