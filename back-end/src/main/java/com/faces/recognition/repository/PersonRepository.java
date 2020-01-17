package com.faces.recognition.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.faces.recognition.model.Person;

public interface PersonRepository extends MongoRepository<Person, Integer>{

	Person findTopByOrderByIdDesc();

	boolean existsByName(String name);

}
