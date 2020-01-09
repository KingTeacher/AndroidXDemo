package com.example.servicelibrary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        String text = getIntent().getStringExtra("tv_text");
        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setText(text);
    }
}
