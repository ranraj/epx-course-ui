package com.cisco.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class Course {	
    private String id;
    private String ownerId;
    private String title;   
    private String name;
    private String photo;
    private List<String> tags;    
    private String category;
    private double cost;
    private int rating;
    private String description;    
    private List<String> likedBy;  
    private float version;
    private boolean deleted;     
    
    public long getLikes() {
    	if(likedBy == null) {
    		return 0L;
    	}
    	return likedBy.size();
    }
}
