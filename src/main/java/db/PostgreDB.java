package db;

import dataObjects.Course;
import dataObjects.Grade;
import dataObjects.GradeId;
import dataObjects.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgreDB implements DB {

  @PersistenceContext
  protected EntityManager entityManager;

  protected Session getCurrentSession() {
    return entityManager.unwrap(Session.class);
  }

  @Override
  public String createStudent(Student student) {
    return performAction(student, this::createObject);
  }

  @Override
  public Student readStudent(long id) {
    return readObject(Student.class, id);
  }

  @Override
  public String updateStudent(Student student) {
    return performAction(student, this::updateStudent);
  }

  private Void updateStudent(FunctionInput<Student> input) {
    Student student = input.getSession().find(Student.class, input.getT().getId());
    if (student == null) {
      throw new ObjectNotFoundException();
    }
    input.getSession().evict(student);
    student.setFirstName(input.getT().getFirstName());
    student.setLastName(input.getT().getLastName());
    student.setEmail(input.getT().getEmail());
    input.getSession().update(student);
    return null;
  }

  @Override
  public String deleteStudent(long id) {
    return performAction(id, this::deleteStudent);
  }

  private Void deleteStudent(FunctionInput<Long> input) {
    return deleteObject(Student.class, input);
  }

  @Override
  public String createCourse(Course course) {
    return performAction(course, this::createObject);
  }

  @Override
  public Course readCourse(long id) {
    return readObject(Course.class, id);
  }

  @Override
  public String updateCourse(Course course) {
    return performAction(course, this::updateCourse);
  }

  private Void updateCourse(FunctionInput<Course> input) {
    Course course = input.getSession().find(Course.class, input.getT().getId());
    if (course == null) {
      throw new ObjectNotFoundException();
    }
    input.getSession().evict(course);
    course.setName(input.getT().getName());
    course.setStudents(input.getT().getStudents());
    input.getSession().update(course);
    return null;
  }

  @Override
  public String deleteCourse(long id) {
    return performAction(id, this::deleteCourse);
  }

  private Void deleteCourse(FunctionInput<Long> input) {
    return deleteObject(Course.class, input);
  }

  @Override
  public String createGrade(Grade grade) {
    return performAction(grade, this::createObject);
  }

  @Override
  public Grade readGrade(GradeId id) {
    return readObject(Grade.class, id);
  }

  @Override
  public String updateGrade(Grade grade) {
    return performAction(grade, this::updateGrade);
  }

  private Void updateGrade(FunctionInput<Grade> input) {
    Grade grade = input.getSession().find(Grade.class, input.getT().getId());
    if (grade == null) {
      throw new ObjectNotFoundException();
    }
    input.getSession().evict(grade);
    grade.setGrade(input.getT().getGrade());
    input.getSession().update(grade);
    return null;
  }

  @Override
  public String deleteGrade(GradeId id) {
    return performAction(id, this::deleteGrade);
  }

  private Void deleteGrade(FunctionInput<GradeId> input) {
    return deleteObject(Grade.class, input);
  }

  private Void createObject(FunctionInput<?> input) {
    input.getSession().save(input.getT());
    return null;
  }

  private <T> T readObject(Class<T> cls, Object id) {
    T t;
    Transaction tx = null;
    try {
      Session session = getCurrentSession();
      tx = session.beginTransaction();
      t = session.find(cls, id);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw  e;
    }
    return t;
  }

  private Void deleteObject(Class<?> cls, FunctionInput<?> input) {
    Object o = input.getSession().find(cls, input.getT());
    if (o == null) {
      throw new ObjectNotFoundException();
    }
    input.getSession().delete(o);
    return null;
  }

  private <T> String performAction(T t, Function<FunctionInput<T>, Void> method) {
    Transaction tx = null;
    try {
      Session session = getCurrentSession();
      tx = session.beginTransaction();
      method.apply(new FunctionInput<>(session, t));
      tx.commit();
    } catch (ObjectNotFoundException e) {
      tx.rollback();
      return OBJECT_NOT_FOUND;
    } catch (Exception e) {
      tx.rollback();
      throw  e;
    }
    return SUCCESS;
  }

  @Override
  public Student getStudentWithHighestAverageInCourse(long courseId) {
    return readStudent(getId(Optional.of(courseId)));
  }

  @Override
  public Course getCourseWithHighestAverage() {
    return readCourse(getId(Optional.empty()));
  }

  private long getId(Optional<Long> courseId) {
    List<Grade> grades;
    Transaction tx = null;
    try {
      Session session = getCurrentSession();
      tx = session.beginTransaction();
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<Grade> cq = cb.createQuery(Grade.class);
      Root<Grade> root = cq.from(Grade.class);
      courseId.ifPresent(id -> cq.select(root).where(cb.equal(root.get("course_id"), id)));
      TypedQuery<Grade> gradesQuery = session.createQuery(cq);
      grades = gradesQuery.getResultList();
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw  e;
    }
    return getId(grades.stream(), grade -> grade.getId().getStudentId());
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
