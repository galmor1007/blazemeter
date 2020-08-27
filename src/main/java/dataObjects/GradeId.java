package dataObjects;

import java.util.Objects;

public class GradeId {

  private long studentId;
  private long courseId;

  public GradeId() {
  }

  public GradeId(long studentId, long courseId) {
    this.studentId = studentId;
    this.courseId = courseId;
  }

  public long getStudentId() {
    return studentId;
  }

  public long getCourseId() {
    return courseId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GradeId gradeId = (GradeId) o;
    return Objects.equals(studentId, gradeId.studentId) &&
            Objects.equals(courseId, gradeId.courseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId);
  }
}
