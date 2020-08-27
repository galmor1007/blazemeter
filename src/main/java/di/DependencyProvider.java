package di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class DependencyProvider {

  private static Injector injector;

  public static void init(Module... modules) {
    injector = Guice.createInjector(modules);
  }

  public static Injector getInjector() {
    return injector;
  }
}
