package com.on.spring.config;

import com.on.spring.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
@ComponentScan
public class TestConfig {

    @Bean
    @Scope("prototype")
    public Person person() {
        return new Person("zhangsan", 10);
    }

    @Bean
    public MyFactoryBean myFactoryBean() {
        return new MyFactoryBean();
    }

}
