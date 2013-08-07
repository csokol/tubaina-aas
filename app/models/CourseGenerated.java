package models;

import java.util.Date;

public class CourseGenerated {
	public final Date lastInstructorDate;
	public final Date lastStudentDate;
	public final String name;
	public CourseGenerated(Date lastInstructorDate, Date lastStudentDate, String name) {
		this.lastInstructorDate = lastInstructorDate;
		this.lastStudentDate = lastStudentDate;
		this.name = name;
	}
}
