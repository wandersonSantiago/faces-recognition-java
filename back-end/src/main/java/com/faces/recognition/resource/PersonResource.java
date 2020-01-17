package com.faces.recognition.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.faces.recognition.model.Person;
import com.faces.recognition.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonResource {
	
	@Autowired
	private PersonService service;
	
	
	@PostMapping
	@ResponseBody
	public Person insert(@RequestBody Person person) {		
		service.insert(person);
		return person;
	}
	
	@GetMapping
	public List<Person> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable Integer id){
		return service.findById(id);
	}

}