package com.example.module_mvvm.model.bean;

public class UserProfile {
    private String id;
    private String name;
    private String age;
    private String sex;
    private String cityName;
    private String cityId;
    private String phone;
    private int workYears;
    private String email;
    private String headImg;

    public UserProfile(String id, String name, String age, String sex, String cityName, String cityId, String phone, int workYears, String email, String headImg) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.cityName = cityName;
        this.cityId = cityId;
        this.phone = phone;
        this.workYears = workYears;
        this.email = email;
        this.headImg = headImg;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWorkYears() {
        return workYears;
    }

    public void setWorkYears(int workYears) {
        this.workYears = workYears;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
