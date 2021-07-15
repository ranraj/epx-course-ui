package com.cisco.epx.course.app.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class TextSimilarityScore {
	private int time;
	private double similarity;
	private String lang;
	private float langConfidence;
	private String timestamp;
}
