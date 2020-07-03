package com.example.module_mvvm.pageState;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.module_mvvm.model.bean.CellModel;
import com.example.module_mvvm.model.bean.UserProfile;

import java.util.List;

public class UserProfilePageState extends ViewModel {

    private UserProfile userProfile;

    private CellModel nameCell;

    private List<CellModel> cellModels;

    private List<View> cellView;


}
