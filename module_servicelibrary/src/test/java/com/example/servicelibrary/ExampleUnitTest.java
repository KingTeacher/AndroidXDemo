package com.example.servicelibrary;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        String pattern = "^[\\u4e00-\\u9fa5A-Za-z0-9\\s——_/-]{2,20}$";
        String pattern2 = "^[\\u4e00-\\u9fa5\\w\\s——_/-]{2,20}$";
        String str1 = "1 2";
        String str2 = "123、";
        String str3 = "adb12/";
        String str4 = "你好-";
        String str5 = "数据——_-/";
        System.out.println("str1结果："+Pattern.matches(pattern, str1));
        System.out.println("str2结果："+Pattern.matches(pattern, str2));
        System.out.println("str3结果："+Pattern.matches(pattern, str3));
        System.out.println("str4结果："+Pattern.matches(pattern, str4));
        System.out.println("str5结果："+Pattern.matches(pattern, str5));
        System.out.println("---------黄金分割线----------");
        System.out.println("str1结果："+Pattern.matches(pattern2, str1));
        System.out.println("str2结果："+Pattern.matches(pattern2, str2));
        System.out.println("str3结果："+Pattern.matches(pattern2, str3));
        System.out.println("str4结果："+Pattern.matches(pattern2, str4));
        System.out.println("str5结果："+Pattern.matches(pattern2, str5));
    }
}