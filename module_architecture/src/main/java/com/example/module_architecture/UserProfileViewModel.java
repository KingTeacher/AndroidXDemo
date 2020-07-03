package com.example.module_architecture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {
    public UserProfileViewModel(String id) {
        this.id = id;
    }

    private String id;
    private LiveData<User> user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
