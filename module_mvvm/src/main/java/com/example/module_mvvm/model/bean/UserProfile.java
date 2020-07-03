package com.example.module_mvvm.model.bean;

import androidx.annotation.NonNull;

public class UserProfile implements Cloneable {
    private String id;
    private String name;
    private String age;
    private String sex;
    private String cityName;
    private String phone;

    public UserProfile() {
    }

    public UserProfile(String id, String name, String age, String sex, String cityName, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.cityName = cityName;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}