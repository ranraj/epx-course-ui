package com.cisco.epx.course.app.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import com.cisco.epx.course.app.entity.CourseSourceType;

@Entity
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
        
    @Column(name = "source_type")
    private CourseSourceType sourceType = CourseSourceType.Youtube;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    private String name;
    
    @NotBlank(message = "Video link is mandatory")
    @Column(name = "video_link")
    private String video;

    @Column(name = "user_id")
    private long userId;

    public Course() {
    }

    public Course(long id, String name, String video, long userId) {
        this.id = id;        
        this.name = name;
        this.video = video;
        this.userId = userId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CourseSourceType getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(CourseSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Course id(long id) {
        setId(id);
        return this;
    }

    public Course sourceType(CourseSourceType sourceType) {
        setSourceType(sourceType);
        return this;
    }

    public Course name(String name) {
        setName(name);
        return this;
    }

    public Course video(String video) {
        setVideo(video);
        return this;
    }

    public Course userId(long userId) {
        setUserId(userId);
        return this;
    }
              
    
}