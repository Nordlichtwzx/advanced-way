package com.on.spring.test;

import com.on.spring.controller.UserController;
import com.on.spring.service.UserService;
import org.junit.Test;

import java.lang.reflect.Field;

public class MyTest {
    @Test
    public void test() throws Exception {

        UserController userController = new UserController();
        Class<? extends UserController> clazz = userController.getClass();
        UserService userService = new UserService();
        Field declaredField = clazz.getDeclaredField("userService");
        declaredField.setAccessible(true);
        Class<?> type = declaredField.getType();
        Object o = type.newInstance();
        declaredField.set(userController, o);
/*        String fieldName = declaredField.getName();
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String methodName = "set" + fieldName;
        Method method = clazz.getMethod(methodName, UserService.class);
        method.invoke(userController, userService);*/
        System.out.println(userController.getUserService());
    }

}
