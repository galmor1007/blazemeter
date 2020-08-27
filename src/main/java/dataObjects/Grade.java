package dataObjects;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "grades")
public class Grade {

  @Column(name = "grade")
  private int grade;
  @Id
  @Embedded
  private GradeId id;

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

  public GradeId getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Grade grade = (Grade) o;
    return Objects.equals(id, grade.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
