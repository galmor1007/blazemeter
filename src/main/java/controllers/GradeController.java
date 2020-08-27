package controllers;

import dataObjects.Grade;
import db.DB;
import di.DependencyProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import services.GradeService;

@Controller
public class GradeController {

  GradeService service = DependencyProvider.getInjector().getInstance(GradeService.class);

  @PostMapping("/create-student")
  @ResponseBody
  public ResponseEntity<String> createCourse(@RequestBody Grade grade) {
    return getResponseEntity(service.createGrade(grade));
  }

  @GetMapping("/read-student")
  @ResponseBody
  public ResponseEntity<Grade> readCourse(@RequestParam(name="student-id") long studentId, @RequestParam(name="course-id") long courseId) {
    Grade grade = service.readGrade(studentId, courseId);
    return grade != null ? new ResponseEntity<>(grade, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/update-student")
  @ResponseBody
  public ResponseEntity<String> updateCourse(@RequestBody Grade grade) {
    return getResponseEntity(service.updateGrade(grade));
  }

  @DeleteMapping("/delete-student")
  @ResponseBody
  public ResponseEntity<String> deleteCourse(@RequestParam(name="student-id") long studentId, @RequestParam(name="course-id") long courseId) {
    return getResponseEntity(service.deleteGrade(studentId, courseId));
  }

  private ResponseEntity<String> getResponseEntity(String s) {
    switch (s) {
      case DB.SUCCESS:
        return new ResponseEntity<>(HttpStatus.CREATED);
      case DB.GRADE_ID_EXISTS:
      case DB.STUDENT_ID_DOES_NOT_EXIST:
      case DB.COURSE_ID_DOES_NOT_EXIST:
      case GradeService.GRADE_TOO_LOW:
      case GradeService.GRADE_TOO_HIGH:
        return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
      case DB.GRADE_ID_DOES_NOT_EXIST:
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
      default:
        return new ResponseEntity<>("Unexpected Result: " + s, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
