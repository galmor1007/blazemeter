package controllers;

import dataObjects.Student;
import db.DB;
import di.DependencyProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import services.StudentService;

@Controller
public class StudentController {

  StudentService service = DependencyProvider.getInjector().getInstance(StudentService.class);

  @PostMapping("/create-student")
  @ResponseBody
  public ResponseEntity<String> createStudent(@RequestBody Student student) {
    return getResponseEntity(service.createStudent(student));
  }

  @GetMapping("/read-student")
  @ResponseBody
  public ResponseEntity<Student> readStudent(@RequestParam(name="id") long id) {
    Student student = service.readStudent(id);
    return student != null ? new ResponseEntity<>(student, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/update-student")
  @ResponseBody
  public ResponseEntity<String> updateStudent(@RequestBody Student student) {
    return getResponseEntity(service.updateStudent(student));
  }

  @DeleteMapping("/delete-student")
  @ResponseBody
  public ResponseEntity<String> deleteStudent(@RequestParam(name="id") long id) {
    return getResponseEntity(service.deleteStudent(id));
  }

  @GetMapping("/best-student-in-course")
  @ResponseBody
  public ResponseEntity<Student> bestStudentInCourse(@RequestParam(name="course-id") long courseId) {
    Student bestInCourse = service.bestStudentInCourse(courseId);
    return bestInCourse != null ? new ResponseEntity<>(bestInCourse, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<String> getResponseEntity(String s) {
    switch (s) {
      case DB.SUCCESS:
        return new ResponseEntity<>(HttpStatus.CREATED);
      case DB.STUDENT_ID_EXISTS:
      case StudentService.BAD_STUDENT_ID:
        return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
      case DB.STUDENT_ID_DOES_NOT_EXIST:
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
      default:
        return new ResponseEntity<>("Unexpected Result: " + s, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
