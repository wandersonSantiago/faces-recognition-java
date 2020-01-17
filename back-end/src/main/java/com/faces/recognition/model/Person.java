package com.faces.recognition.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Person {


	@Id
	private Integer id;
	@Size(min = 5, max =  100, message = "Nome tem que ter no minimo 5 e no máximo 100 caracteres")
	private String  name;
	@NotNull
	private byte[] photo;
	
	public Person() {
	}
	
	public Person(String name, byte[] photo) {
		this.name = name;
		this.photo = photo;
	}
	
	public void setName(String name) {
		if(name.isBlank()) {
			throw new DataIntegrityViolationException("Nome tem que ter no minimo 5 e no máximo 100 caracteres");
		}
		this.name = name;
	}
}
