package db;

import org.hibernate.Session;

public class FunctionInput<T> {

  private Session session;
  private T t;

  public FunctionInput(Session session, T t) {
    this.session = session;
    this.t = t;
  }

  public Session getSession() {
    return session;
  }

  public T getT() {
    return t;
  }
}
