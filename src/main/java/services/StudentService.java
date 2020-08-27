package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dataObjects.Student;
import db.DB;
import org.springframework.util.StringUtils;
import regex.Regex;

@Singleton
public class StudentService {

  public static final String BAD_STUDENT_ID = "Student ID must be positive";
  public static final String EMPTY_FIRST_NAME = "Student first name cannot be null or empty";
  public static final String EMPTY_LAST_NAME = "Student last name cannot be null or empty";
  public static final String INVALID_EMAIL = "Student email invalid";
  public static final String VALID = "Valid";

  private final DB db;

  @Inject
  public StudentService(DB db) {
    this.db = db;
  }

  public String createStudent(Student student) {
    if (student.getId() <= 0) {
      return BAD_STUDENT_ID;
    }
    String validationResult = validateStudentData(student);
    return validationResult.equals(VALID) ? db.createStudent(student) : validationResult;
  }

  public Student readStudent(long id) {
    return db.readStudent(id);
  }

  public String updateStudent(Student student) {
    String validationResult = validateStudentData(student);
    return validationResult.equals(VALID) ? db.updateStudent(student) : validationResult;
  }

  public String deleteStudent(long id) {
    return db.deleteStudent(id);
  }

  public Student bestStudentInCourse(long courseId) {
    return db.getStudentWithHighestAverageInCourse(courseId);
  }

  private String validateStudentData(Student student) {
    if (StringUtils.isEmpty(student.getFirstName())) {
      return EMPTY_FIRST_NAME;
    }
    if (StringUtils.isEmpty(student.getLastName())) {
      return EMPTY_LAST_NAME;
    }
    if (StringUtils.isEmpty(student.getEmail()) || !student.getEmail().matches(Regex.email)) {
      return INVALID_EMAIL;
    }
    return VALID;
  }
}
