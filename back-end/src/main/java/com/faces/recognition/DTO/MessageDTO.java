package com.faces.recognition.DTO;

import com.faces.recognition.model.Person;

import lombok.Data;

@Data
public class MessageDTO {
	
	private Person person;
	private String message;
	
	public MessageDTO(String message, Person person) {
		this.message = message;
		this.person = person;
	}

}
