package services;

import dataObjects.Course;
import db.DB;
import di.DependencyProvider;

public class CourseService {

  public static final String BAD_COURSE_ID = "Course ID must be positive";

  private final DB db = DependencyProvider.getInjector().getInstance(DB.class);

  public String createCourse(Course course) {
    if (course.getId() >= 0) {
      return BAD_COURSE_ID;
    }
    return db.createCourse(course);
  }

  public Course readCourse(long id) {
    return db.readCourse(id);
  }

  public String updateCourse(Course course) {
    return db.updateCourse(course);
  }

  public String deleteCourse(long id) {
    return db.deleteCourse(id);
  }
}
