package org.example.camunda.process.solution;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableZeebeClient
@EnableScheduling
@Deployment(resources = "classpath*:/models/*.*")
public class ProcessApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProcessApplication.class, args);
  }

  @Bean
  public OpenAPI swaggerOpenAPI() {
    return new OpenAPI();
  }
}
