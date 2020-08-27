package dataObjects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Course {

  private long id;
  private String name;
  private Collection<Student> students = new HashSet<>();

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Collection<Student> getStudents() {
    return students;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return id == course.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
