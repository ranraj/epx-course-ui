package com.cisco.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class ChapterQuestion {
    	
    private String id;
    private String description;
    private String chapterId;            
    private AnswerType answerType;
    private List<AnswerChoise> choices;
    private String answer;
    private String createdBy;
    //private Date created;
    //private Date updated;
    private String updatedBy;  
    private float version;
    
}
