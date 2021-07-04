package com.cisco.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
	
	private String id;	
	private String email;
	private String password;	
	private String username;
	private List<String> favorites;
}
