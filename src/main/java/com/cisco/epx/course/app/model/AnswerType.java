package com.cisco.epx.course.app.model;

public enum AnswerType {
	CHOICE("Choice"),
    FILLUP("Fillup"), 
    MULTIPLE_CHOICE("MultipleChoice"), 
    MATCH("Match");
    
	private String text;

	AnswerType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static AnswerType fromString(String text) {
        for (AnswerType b : AnswerType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}