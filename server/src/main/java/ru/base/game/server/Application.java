package ru.base.game.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
