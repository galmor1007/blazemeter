package db;

import dataObjects.Course;
import dataObjects.Grade;
import dataObjects.GradeId;
import dataObjects.Student;

public interface DB {

  String SUCCESS = "Success";
  String STUDENT_ID_EXISTS = "Student ID exists";
  String COURSE_CONTAINS_NON_EXISTING_STUDENTS = "Course contains non-existing students";
  String COURSE_ID_EXISTS = "Course ID exists";
  String GRADE_ID_EXISTS = "This student already has a grade in this course";
  String OBJECT_NOT_FOUND = "Requested object not found";

  // CRUD
  String createStudent(Student student);
  Student readStudent(long id);
  String updateStudent(Student student);
  String deleteStudent(long id);

  String createCourse(Course course);
  Course readCourse(long id);
  String updateCourse(Course course);
  String deleteCourse(long id);

  String createGrade(Grade grade);
  Grade readGrade(GradeId id);
  String updateGrade(Grade grade);
  String deleteGrade(GradeId id);

  // Queries
  Student getStudentWithHighestAverageInCourse(long courseId);
  Course getCourseWithHighestAverage();
}
