package db;

import dataObjects.Course;
import dataObjects.Grade;
import dataObjects.GradeId;
import dataObjects.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDB implements DB {

  private final Map<Long, Student> students = new HashMap<>();
  private final Map<Long, Course> courses = new HashMap<>();
  private final Map<GradeId, Grade> grades = new HashMap<>();

  @Override
  public synchronized String createStudent(Student student) {
    return students.putIfAbsent(student.getId(), student) == null ? SUCCESS : STUDENT_ID_EXISTS;
  }

  @Override
  public synchronized Student readStudent(long id) {
    return students.get(id);
  }

  @Override
  public synchronized String updateStudent(Student student) {
    if (!students.containsKey(student.getId())) {
      return OBJECT_NOT_FOUND;
    }
    students.put(student.getId(), student);
    return SUCCESS;
  }

  @Override
  public synchronized String deleteStudent(long id) {
    if (!students.containsKey(id)) {
      return OBJECT_NOT_FOUND;
    }
    grades.keySet().stream().filter(gradeId -> gradeId.getStudentId() == id).forEach(grades::remove);
    courses.values().forEach(course -> course.getStudents().removeAll(course.getStudents().stream().filter(student -> student.getId() == id).collect(Collectors.toSet())));
    students.remove(id);
    return SUCCESS;
  }

  @Override
  public synchronized String createCourse(Course course) {
    for (Student student : course.getStudents()) {
      if (!students.containsKey(student.getId())) {
        return COURSE_CONTAINS_NON_EXISTING_STUDENTS;
      }
    }
    return courses.putIfAbsent(course.getId(), course) == null ? SUCCESS : COURSE_ID_EXISTS;
  }

  @Override
  public synchronized Course readCourse(long id) {
    return courses.get(id);
  }

  @Override
  public synchronized String updateCourse(Course course) {
    if (!courses.containsKey(course.getId())) {
      return OBJECT_NOT_FOUND;
    }
    Course preUpdate = courses.remove(course.getId());
    String updateResult = createCourse(course);
    if (updateResult.equals(SUCCESS)) {
      return SUCCESS;
    }
    courses.put(course.getId(), preUpdate);
    return updateResult;
  }

  @Override
  public synchronized String deleteCourse(long id) {
    if (!courses.containsKey(id)) {
      return OBJECT_NOT_FOUND;
    }
    grades.keySet().stream().filter(gradeId -> gradeId.getCourseId() == id).forEach(grades::remove);
    courses.remove(id);
    return SUCCESS;
  }

  @Override
  public synchronized String createGrade(Grade grade) {
    if (!students.containsKey(grade.getId().getStudentId()) || !courses.containsKey(grade.getId().getCourseId())) {
      return OBJECT_NOT_FOUND;
    }
    return grades.putIfAbsent(grade.getId(), grade) == null ? SUCCESS : GRADE_ID_EXISTS;
  }

  @Override
  public synchronized Grade readGrade(GradeId id) {
    return grades.get(id);
  }

  @Override
  public synchronized String updateGrade(Grade grade) {
    if (!grades.containsKey(grade.getId())) {
      return OBJECT_NOT_FOUND;
    }
    Grade preUpdate = grades.get(grade.getId());
    String updateResult = createGrade(grade);
    if (updateResult.equals(SUCCESS)) {
      return SUCCESS;
    }
    grades.put(grade.getId(), preUpdate);
    return updateResult;
  }

  @Override
  public synchronized String deleteGrade(GradeId id) {
    return grades.remove(id) != null ? SUCCESS : OBJECT_NOT_FOUND;
  }

  @Override
  public synchronized Student getStudentWithHighestAverageInCourse(long courseId) {
    if (!courses.containsKey(courseId) || courses.get(courseId).getStudents().isEmpty()) {
      return null;
    }
    return readStudent(getId(grades.values().stream().filter(grade -> grade.getId().getCourseId() == courseId), grade -> grade.getId().getStudentId()));
  }

  @Override
  public synchronized Course getCourseWithHighestAverage() {
    return readCourse(getId(grades.values().stream(), grade -> grade.getId().getCourseId()));
  }

  private long getId(Stream<Grade> relevantGrades, Function<Grade, Long> groupingFunction) {
    Map<Long, List<Grade>> map = relevantGrades.collect(Collectors.groupingBy(groupingFunction));
    long bestId = -1;
    double highestAverage = 0;
    for (Map.Entry<Long, List<Grade>> entry : map.entrySet()) {
      double avg = entry.getValue().stream().mapToInt(Grade::getGrade).average().orElse(0);
      if (avg > highestAverage) {
        highestAverage = avg;
        bestId = entry.getKey();
      }
    }
    return bestId;
  }
}
