package com.on.mybatis.test;

import com.on.mybatis.entity.User;
import com.on.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;


public class TestMyBatis {
    @Test
    public void test01() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            User selectUser = session.selectOne("selectUser", 1);
            System.out.println(selectUser);
        }
    }


    @Test
    public void test02() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            System.out.println(mapper.selectUser(1));
        }
    }

    @Test
    public void tt() {
        int i = strStr("mississippi", "sippia");
        System.out.println(i);
    }


    public int strStr(String haystack, String needle) {
        Queue queue = new LinkedList<>();
        if ("".equals(needle)) {
            return 0;
        }
        char needleStart = needle.charAt(0);
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needleStart) {
                String temp = "";
                if (i + needle.length() > haystack.length()) {
                    temp = haystack.substring(i);
                } else {
                    temp = haystack.substring(i, i + needle.length());
                }
                if (temp.equals(needle)) {
                    return i;
                }
            }
            if (i > haystack.length() - needle.length()) {
                break;
            }
        }
        return -1;
    }


}
