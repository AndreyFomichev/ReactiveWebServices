package io.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableEurekaClient
//@EnableJpaRepositories("io.rest.jpa.repository")
//@EntityScan("io.rest.jpa.entities")
public class ReactiveProg {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ReactiveProg.class, args);
        MyWebClient gwc = new MyWebClient("/external/", "102", "203");
        Thread t = new Thread(gwc);
        t.start();
        System.out.println(gwc.getResult());
    }

}

