package com.example.module_mvvm;

import android.os.Bundle;
import android.view.View;

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

    private UserProfileViewModel viewModel;
    private ActivityContainerBinding viewBinding;
    private List<CellModel> cellModelList = new ArrayList<>();
    private ContainerAdapter containerAdapter;
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initData();
        initView();
    }

    private void initData() {
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        //监听cell数据是否变化
        viewModel.getCellModelsData().observe(this, cellModels -> {
            cellModelList.clear();
            cellModelList.addAll(cellModels);
            containerAdapter.notifyDataSetChanged();
        });
    }

    private void initView() {
        viewBinding.ryView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        containerAdapter = new ContainerAdapter(cellModelList);
        viewBinding.ryView.setAdapter(containerAdapter);

        viewBinding.btSave.setOnClickListener(view -> {
            boolean b = viewModel.saveInfo();
            if (b){
                finish();
            }
        });
    }

}
