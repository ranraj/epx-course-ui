package com.ran.epx.course.app.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Expected urls format
 * 	https://www.youtube.com/embed/zrJC7mtUq1E
 *  https://www.youtube.com/watch?v=zrJC7mtUq1E
 * @author randhana
 *
 */
@Data
@Accessors
public class ChapterContent {
	private String youTubeVideoLink;
	private String youTubeVideoId;
}
