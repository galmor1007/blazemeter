package di;

import com.google.inject.AbstractModule;
import db.DB;
import db.LocalDB;

public class LocalDBModule extends AbstractModule {

  @Override
  protected void configure() {
    LocalDB localDB = new LocalDB();
    bind(DB.class).toInstance(localDB);
  }
}
