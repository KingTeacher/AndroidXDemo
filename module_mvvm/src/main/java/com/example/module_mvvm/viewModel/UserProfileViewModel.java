package com.example.module_mvvm.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.module_mvvm.EditActivity;
import com.example.module_mvvm.model.UserRepository;
import com.example.module_mvvm.model.bean.CellModel;
import com.example.module_mvvm.model.bean.UserProfile;
import com.example.module_mvvm.model.UserProfilePageStatus;
import com.example.module_mvvm.view.CheckDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {
    //用户个人资料
    private MutableLiveData<UserProfile> userProfileData;
    //页面配置信息
    private LiveData<UserProfilePageStatus> userProfilePageStatus;
    //解析后的用户个人资料数据
    private MutableLiveData<List<CellModel>> cellModelsData;


    public MutableLiveData<UserProfile> getUserProfileData() {
        if (userProfileData == null){
            userProfileData = new MutableLiveData<>();
            UserProfile UserProfile = null;
            try {
                UserProfile = (UserProfile) UserRepository.getUserProfile().clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            userProfileData.setValue(UserProfile);
        }
        return userProfileData;
    }

    public void setUserProfileData(UserProfile userProfileBean) {
        if (userProfileData == null){
            userProfileData = new MutableLiveData<>();
        }
        this.userProfileData.setValue(userProfileBean);
    }

    public LiveData<UserProfilePageStatus> getUserProfilePageStatus() {
        if (userProfilePageStatus == null){
            userProfilePageStatus = new MutableLiveData<>();
        }
        return userProfilePageStatus;
    }

    public LiveData<List<CellModel>> getCellModelsData() {
        if (cellModelsData == null){
            cellModelsData = new MutableLiveData<>();
            cellModelsData.setValue(getUserProfileStatusByUser(getUserProfileData().getValue()));
        }
        return cellModelsData;
    }

    private List<CellModel> getUserProfileStatusByUser(UserProfile userProfile){
        List<CellModel> cellModels = new ArrayList<>();
        if (userProfile == null) return cellModels;
        // TODO: 2020/7/2 tips提示没法动态更改
        cellModels.add(new CellModel("姓名",userProfile.getName(),false, v -> {
            if (v.getContext() instanceof Activity) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                ((Activity) v.getContext()).startActivityForResult(intent, 11);
            }
        }));
        cellModels.add(new CellModel("性别",userProfile.getSex(),true, v -> {
            if (v.getContext() instanceof FragmentActivity) {
                CheckDialogFragment checkDialogFragment = new CheckDialogFragment();
                checkDialogFragment.setCallback(s ->{
                    cellModels.get(1).setValue(s);
                    cellModelsData.setValue(cellModels);
                });
                checkDialogFragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(),"dialog1");
            }
        }));
        cellModels.add(new CellModel("出生年月",userProfile.getAge(),true, v -> {
            if (v.getContext() instanceof FragmentActivity) {
                CheckDialogFragment checkDialogFragment = new CheckDialogFragment();
                checkDialogFragment.setCallback(s ->{
                    cellModels.get(2).setValue(s);
                    cellModelsData.setValue(cellModels);
                });
                checkDialogFragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(),"dialog2");
            }
        }));
        cellModels.add(new CellModel("电话",userProfile.getPhone(),true, v -> {
            if (v.getContext() instanceof FragmentActivity) {
                CheckDialogFragment checkDialogFragment = new CheckDialogFragment();
                checkDialogFragment.setCallback(s ->{
                    cellModels.get(3).setValue(s);
                    cellModelsData.setValue(cellModels);
                });
                checkDialogFragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(),"dialog3");
            }
        }));
        return cellModels;
    }

    public boolean saveInfo(){
        boolean isEmpty = false;
        for (CellModel cellModel : getCellModelsData().getValue()) {
            if (TextUtils.isEmpty(cellModel.getValue())) {
                cellModel.setTips("不能为空");
                cellModel.setShowError(true);
                isEmpty = true;
            }else{
                cellModel.setShowError(true);
            }
        }
        cellModelsData.setValue(getCellModelsData().getValue());
        if (!isEmpty){
            // TODO: 2020/7/3 更新当前数据
            userProfileData.getValue().setName(cellModelsData.getValue().get(0).getValue());
            userProfileData.getValue().setAge(cellModelsData.getValue().get(1).getValue());
            userProfileData.getValue().setSex(cellModelsData.getValue().get(2).getValue());
            userProfileData.getValue().setPhone(cellModelsData.getValue().get(3).getValue());
            // TODO: 2020/7/3 保存数据
            UserRepository.setUserProfile(userProfileData.getValue());
            return true;
        }

        return false;

    }

}
