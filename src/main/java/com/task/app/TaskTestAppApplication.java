package com.task.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskTestAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTestAppApplication.class, args);
    }

}
