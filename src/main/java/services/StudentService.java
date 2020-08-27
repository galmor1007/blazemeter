package services;

import com.google.inject.Singleton;
import dataObjects.Student;
import db.DB;
import di.DependencyProvider;

@Singleton
public class StudentService {

  public static final String BAD_STUDENT_ID = "Student ID must be positive";

  private final DB db = DependencyProvider.getInjector().getInstance(DB.class);

  public String createStudent(Student student) {
    if (student.getId() >= 0) {
      return BAD_STUDENT_ID;
    }
    return db.createStudent(student);
  }

  public Student readStudent(long id) {
    return db.readStudent(id);
  }

  public String updateStudent(Student student) {
    return db.updateStudent(student);
  }

  public String deleteStudent(long id) {
    return db.deleteStudent(id);
  }

  public Student bestStudentInCourse(long courseId) {
    return db.getStudentWithHighestAverageInCourse(courseId);
  }
}
