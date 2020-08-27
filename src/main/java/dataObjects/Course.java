package dataObjects;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {

  @Id
  @Column(name = "course_id")
  private long id;
  @Column(name = "course_name")
  private String name;
  @OneToMany
  @JoinColumn(name = "student_id")
  private Collection<Student> students = new HashSet<>();

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Student> getStudents() {
    return students;
  }

  public void setStudents(Collection<Student> students) {
    this.students = students;
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
