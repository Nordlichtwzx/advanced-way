package com.on.spring.config;

import com.on.spring.entity.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        Person person = (Person) annotationConfigApplicationContext.getBean("person");
        Person person2 = (Person) annotationConfigApplicationContext.getBean("person");
        System.out.println(person == person2);
    }

}

