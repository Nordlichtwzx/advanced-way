package com.come.test;

import com.on.spring.config.TestConfig;
import com.on.spring.entity.Person;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        Person person = (Person) annotationConfigApplicationContext.getBean("person");
        Person person2 = (Person) annotationConfigApplicationContext.getBean("person");
        System.out.println(person == person2);

        Object myFactoryBean = annotationConfigApplicationContext.getBean("myFactoryBean");
        Class<?> aClass = myFactoryBean.getClass();
        System.out.println(aClass.getName());
        Object myFactoryBean1 = annotationConfigApplicationContext.getBean("&myFactoryBean");
        Class<?> aClass1 = myFactoryBean1.getClass();
        System.out.println(aClass1.getName());

    }

}
