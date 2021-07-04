package com.cisco.epx.course.app.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class ExamQuestion {
    
	@Id
    private String id;
    private String description;
    private String courseId;            
    private AnswerType answerType;
    private List<AnswerChoise> choices;
    private String answer;
    private String createdBy;
    //private Date created;
    //private Date updated;
    private String updatedBy;  
}
