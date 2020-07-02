package com.example.module_mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.module_mvvm.adapter.ContainerAdapter;
import com.example.module_mvvm.databinding.ActivityContainerBinding;
import com.example.module_mvvm.model.bean.CellModel;
import com.example.module_mvvm.viewModel.UserProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContainerActivity extends AppCompatActivity {

    private UserProfileViewModel userProfileViewModel;
    private ActivityContainerBinding viewbinding;
    private List<CellModel> cellModelList = new ArrayList<>();
    private ContainerAdapter containerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewbinding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(viewbinding.getRoot());
        initData();
        initView();
    }

    private void initData() {
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        //监听cell数据是否变化
        userProfileViewModel.getCellmodels().observe(this, cellModels -> {
            cellModelList.clear();
            cellModelList.addAll(cellModels);
            containerAdapter.notifyDataSetChanged();
        });
    }

    private void initView() {
        viewbinding.ryView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        containerAdapter = new ContainerAdapter(cellModelList);
        viewbinding.ryView.setAdapter(containerAdapter);
    }
}
