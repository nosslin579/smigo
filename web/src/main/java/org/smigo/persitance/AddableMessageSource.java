package org.smigo.persitance;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.IOException;
import java.util.Locale;

//@Component("messageSource")
public class AddableMessageSource extends ReloadableResourceBundleMessageSource {

  public AddableMessageSource() {
    setBasename("/WEB-INF/messages/messages");
    setUseCodeAsDefaultMessage(true);
    // setCacheSeconds(10);
  }

  public void addTranslation(Locale locale, String key, String translation) throws IOException {
    // File f = new File("../webapps/beta/web-inf/messages/messages_" +
    // locale + ".properties");
    // FileWriter os = new FileWriter(f, true);
    // os.write(key + "=" + translation + "\n");
    // os.close();
  }
}