package com.example.module_mvvm.model;

import com.example.module_mvvm.model.bean.UserProfile;

public class UserRepository {

    private static UserProfile userProfile;

    public static UserProfile getUserProfile() {
        if (userProfile == null){
            userProfile = new UserProfile("111", "花千骨", "11", "女", "大宋", "12312312312");
        }
        return userProfile;
    }

    public static void setUserProfile(UserProfile userProfile) {
        UserRepository.userProfile = userProfile;
    }
}
