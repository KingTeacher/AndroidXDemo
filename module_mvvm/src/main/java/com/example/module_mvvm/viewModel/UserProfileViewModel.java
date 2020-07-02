package com.example.module_mvvm.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.module_mvvm.model.UserRepository;
import com.example.module_mvvm.model.bean.CellModel;
import com.example.module_mvvm.model.bean.UserProfile;
import com.example.module_mvvm.model.UserProfilePageStatus;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private String userId;
    //用户个人资料
    private MutableLiveData<UserProfile> userProfile;
    //页面配置信息
    private LiveData<UserProfilePageStatus> userProfilePageStatus;
    //解析后的用户个人资料数据
    private LiveData<List<CellModel>> cellmodels;

    public MutableLiveData<UserProfile> getUserProfile() {
        if (userProfile == null){
            userProfile = new MutableLiveData<>();
        }
        userProfile.setValue(UserRepository.getUserProfile());
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile.setValue( userProfile);
    }

    public LiveData<UserProfilePageStatus> getUserProfilePageStatus() {
        if (userProfilePageStatus == null){
            userProfilePageStatus = new MutableLiveData<>();
        }
        return userProfilePageStatus;
    }

    public LiveData<List<CellModel>> getCellmodels() {
        if (cellmodels == null){
            cellmodels = Transformations.map(getUserProfile(), this::getUserProfileStatusByUser);
        }
        return cellmodels;
    }

    private List<CellModel> getUserProfileStatusByUser(UserProfile userProfile){
        List<CellModel> cellModels = new ArrayList<>();
        // TODO: 2020/7/2 tips提示没法动态更改
        cellModels.add(new CellModel("姓名",userProfile.getName(),false,false,"数据不能为空"));
        cellModels.add(new CellModel("性别",userProfile.getSex(),true,false,"数据不能为空"));
        cellModels.add(new CellModel("出生年月",userProfile.getAge(),true,false,"数据不能为空"));
        cellModels.add(new CellModel("电话",userProfile.getPhone(),true,true,"数据不能为空"));
        return cellModels;
    }

}
