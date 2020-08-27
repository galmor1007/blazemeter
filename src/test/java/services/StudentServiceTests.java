package services;

import dataObjects.Student;
import db.DB;
import db.LocalDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class StudentServiceTests {

  StudentService service;

  @BeforeEach
  public void setup() {
    service = new StudentService(new LocalDB());
  }

  @Test
  public void invalidStudentIdTest() {
    Student student = new Student(0, "firstName", "lastName", "email@gmail.com");
    assert service.createStudent(student).equals(StudentService.BAD_STUDENT_ID);
  }

  @Test
  public void emptyStudentFirstNameTest() {
    Student student = new Student(1, "", "lastName", "email@gmail.com");
    assert service.createStudent(student).equals(StudentService.EMPTY_FIRST_NAME);
  }

  @Test
  public void nullStudentFirstNameTest() {
    Student student = new Student(1, null, "lastName", "email@gmail.com");
    assert service.createStudent(student).equals(StudentService.EMPTY_FIRST_NAME);
  }

  @Test
  public void emptyStudentLastNameTest() {
    Student student = new Student(1, "firstName", "", "email@gmail.com");
    assert service.createStudent(student).equals(StudentService.EMPTY_LAST_NAME);
  }

  @Test
  public void nullStudentLastNameTest() {
    Student student = new Student(1, "firstName", null, "email@gmail.com");
    assert service.createStudent(student).equals(StudentService.EMPTY_LAST_NAME);
  }

  @Test
  public void emptyStudentEmailTest() {
    Student student = new Student(1, "firstName", "lastName", "");
    assert service.createStudent(student).equals(StudentService.INVALID_EMAIL);
  }

  @Test
  public void nullStudentEmailTest() {
    Student student = new Student(1, "firstName", "lastName", null);
    assert service.createStudent(student).equals(StudentService.INVALID_EMAIL);
  }

  @Test
  public void addValidStudentTest() {
    Student student = new Student(1, "firstName", "lastName", "email@gmail.com");
    assert service.createStudent(student).equals(DB.SUCCESS);
  }

  @Test
  public void readNonExistingStudent() {
    Student readResult = service.readStudent(1);
    Assert.isTrue(readResult == null, readResult.toString());
  }

  @Test
  public void readExistingStudent() {
    Student student = new Student(1, "firstName", "lastName", "email@gmail.com");
    String createResult = service.createStudent(student);
    Assert.isTrue(createResult.equals(DB.SUCCESS), createResult);
    Student readResult = service.readStudent(1);
    Assert.isTrue(readResult.equals(student), readResult.toString());
  }

  @Test
  public void updateNonExistingStudent() {
    Student student = new Student(1, "firstName", "lastName", "email@gmail.com");
    String updateResult = service.updateStudent(student);
    Assert.isTrue(updateResult.equals(DB.STUDENT_ID_DOES_NOT_EXIST), updateResult);
  }

  // todo - update with invalid values
  // todo - valid update
  // todo - delete non existing user
  // todo - delete existing user and verify deleted
}
