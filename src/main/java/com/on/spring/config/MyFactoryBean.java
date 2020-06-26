package com.on.spring.config;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean<Animal> {
    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Animal getObject() throws Exception {
        return new Animal();
    }

    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }
}
