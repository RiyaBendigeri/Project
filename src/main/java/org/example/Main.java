package org.example;



import org.springframework.boot.SpringApplication;//launches the springboot application
import org.springframework.boot.autoconfigure.SpringBootApplication;//adds all annotations
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication

public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

}
