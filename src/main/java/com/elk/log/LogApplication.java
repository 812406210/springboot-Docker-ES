package com.elk.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LogApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }

}
