package com.piyush;

import com.piyush.features.JarExecutor;
import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot application start class.
 *
 * @author Piyush on 25/10/16.
 */
@SpringBootApplication
@ComponentScan(value = "com.piyush", lazyInit = true)
public class Application implements CommandLineRunner {

  private final JarExecutor jarExecutor;

  /**
   * Initialize the jar service, mail service and Redmine service.
   * 1) Jar Service: For executing jar.
   * 2) Mail service: For sending the mail after jar completion.
   * 3) RedmineService: For updating ticket after jar completion.
   *
   * @param jarExecutor {@link JarExecutor}
   */
  public Application(JarExecutor jarExecutor) {
    this.jarExecutor = jarExecutor;

  }


  /**
   * Main method to start spring boot application.
   *
   * @param args Run time arguments
   */
  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder(Application.class)
        .contextClass(AnnotationConfigApplicationContext.class)
        .run(args);
    Runtime.getRuntime().exit(0);
  }

  @Override
  public void run(String... args) throws IOException {
    jarExecutor.execute();

  }
}