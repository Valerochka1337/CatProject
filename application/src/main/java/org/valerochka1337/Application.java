package org.valerochka1337;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.valerochka1337")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
