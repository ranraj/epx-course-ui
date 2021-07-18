package com.ran.epx.course.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LikeCourseDto {
    private String courseId;
    private String userId;
    
	public LikeCourseDto(String courseId, String userId) {		
		this.courseId = courseId;
		this.userId = userId;
	}        
}
