package com.faces.recognition.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.faces.recognition.model.Person;
import com.faces.recognition.repository.PersonRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class PersonService {

	@Autowired
	private PersonRepository repository;

	@Transactional(readOnly = false)
	public Person insert(Person person) {
		if(verifyNameEquals(person.getName())) {
			throw new DataIntegrityViolationException("Já existe uma pessoa com o mesmo nome cadastrado");
		}
		verifyId(person);		
		return repository.save(person);
	}
	
	private boolean verifyNameEquals(String name) {
		return repository.existsByName(name);
	}

	private void verifyId(Person person) {
		Person personID = repository.findTopByOrderByIdDesc();
		if(personID != null) {
			person.setId(personID.getId() + 1);
		}else {
			person.setId(1);
		}		
	}
	
	public Person findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Person not found!"));
	}

	public List<Person> findAll() {
		return repository.findAll();
	}

}
