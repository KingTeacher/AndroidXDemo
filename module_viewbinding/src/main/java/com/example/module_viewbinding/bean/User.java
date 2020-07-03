package com.example.module_viewbinding.bean;

public class User {
    private String name;
    private String id;
    private String sex;
    public int age;
    public String home;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String iD) {
        this.id = iD;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(String name, String id, String sex, int age, String home) {
        this.name = name;
        this.id = id;
        this.sex = sex;
        this.age = age;
        this.home = home;
    }
}
