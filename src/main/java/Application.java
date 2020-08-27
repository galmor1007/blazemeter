import di.DBModule;
import di.DependencyProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    DependencyProvider.init(new DBModule());
    SpringApplication.run(Application.class, args);
  }
}
