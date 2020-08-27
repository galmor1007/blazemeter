package dataObjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GradeId {

  @Column(name = "student_id")
  private long studentId;
  @Column(name = "course_id")
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
