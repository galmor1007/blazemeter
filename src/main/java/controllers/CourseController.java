package controllers;

import dataObjects.Course;
import db.DB;
import di.DependencyProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import services.CourseService;

@Controller
public class CourseController {

  CourseService service = DependencyProvider.getInjector().getInstance(CourseService.class);

  @PostMapping("/create-student")
  @ResponseBody
  public ResponseEntity<String> createCourse(@RequestBody Course course) {
    return getResponseEntity(service.createCourse(course));
  }

  @GetMapping("/read-student")
  @ResponseBody
  public ResponseEntity<Course> readCourse(@RequestParam(name="id") long id) {
    Course course = service.readCourse(id);
    return course != null ? new ResponseEntity<>(course, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/update-student")
  @ResponseBody
  public ResponseEntity<String> updateCourse(@RequestBody Course course) {
    return getResponseEntity(service.updateCourse(course));
  }

  @DeleteMapping("/delete-student")
  @ResponseBody
  public ResponseEntity<String> deleteCourse(@RequestParam(name="id") long id) {
    return getResponseEntity(service.deleteCourse(id));
  }

  private ResponseEntity<String> getResponseEntity(String s) {
    switch (s) {
      case DB.SUCCESS:
        return new ResponseEntity<>(HttpStatus.CREATED);
      case DB.COURSE_CONTAINS_NON_EXISTING_STUDENTS:
      case DB.COURSE_ID_EXISTS:
      case CourseService.BAD_COURSE_ID:
        return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
      case DB.COURSE_ID_DOES_NOT_EXIST:
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
      default:
        return new ResponseEntity<>("Unexpected Result: " + s, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
