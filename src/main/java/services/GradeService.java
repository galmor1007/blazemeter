package services;

import dataObjects.Grade;
import dataObjects.GradeId;
import db.DB;
import di.DependencyProvider;

public class GradeService {

  public static final String GRADE_TOO_LOW = "Grade cannot be negative";
  public static final String GRADE_TOO_HIGH = "Grade cannot be over 100";

  private final DB db = DependencyProvider.getInjector().getInstance(DB.class);

  public String createGrade(Grade grade) {
    if (grade.getGrade() <= 0) {
      return GRADE_TOO_LOW;
    }
    if (grade.getGrade() > 100) {
      return GRADE_TOO_HIGH;
    }
    return db.createGrade(grade);
  }

  public Grade readGrade(long studentId, long courseId) {
    return db.readGrade(new GradeId(studentId, courseId));
  }

  public String updateGrade(Grade grade) {
    return db.updateGrade(grade);
  }

  public String deleteGrade(long studentId, long courseId) {
    return db.deleteGrade(new GradeId(studentId, courseId));
  }
}
