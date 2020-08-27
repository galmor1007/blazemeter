package dataObjects;

import java.util.Objects;

public class Grade {

  private int grade;
  private GradeId id;

  public int getGrade() {
    return grade;
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
