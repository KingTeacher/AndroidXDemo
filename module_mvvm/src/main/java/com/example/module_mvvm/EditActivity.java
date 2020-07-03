package com.example.module_mvvm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.module_mvvm.databinding.ActivityEditeBinding;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditeBinding editeBinding = ActivityEditeBinding.inflate(getLayoutInflater());
        setContentView(editeBinding.getRoot());
        editeBinding.btSure.setOnClickListener(view ->{
            String s = editeBinding.etValue.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("value",s);
            setResult(RESULT_OK,intent);
            finish();
        });

        editeBinding.btCancel.setOnClickListener(view ->{
            finish();
        });

    }
}
