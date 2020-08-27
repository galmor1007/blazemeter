import di.LocalDBModule;
import di.DependencyProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    DependencyProvider.init(new LocalDBModule());
    SpringApplication.run(Application.class, args);
  }
}
