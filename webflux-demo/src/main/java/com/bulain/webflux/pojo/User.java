package com.bulain.webflux.pojo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
	@Id
	private String id; 
	@Indexed(unique = true)
	private String username;
	private String name;
	private String phone;
	private Date birthday;
}
