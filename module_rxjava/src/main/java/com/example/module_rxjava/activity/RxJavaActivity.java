package com.example.module_rxjava.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.module_rxjava.R;
import com.example.module_rxjava.demo.Demo10;
import com.example.module_rxjava.demo.Demo4;
import com.example.module_rxjava.demo.Demo5;
import com.example.module_rxjava.demo.Demo7;
import com.example.module_rxjava.demo.Demo8;
import com.example.module_rxjava.demo.Demo9;

public class RxJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
    }

    public void demo4_zip_io(View view) {
        Demo4.subscribeZipIO();
    }

    public void demo5_1(View view) {
        Demo5.subscribeZip();
    }

    public void demo5_2(View view) {
        Demo5.subscribe1();
    }

    public void demo5_3(View view) {
        Demo5.subscribe2();
    }

    public void demo7_1(View view) {
        Demo7.subscribe1();
    }

    public void demo7_2(View view) {
        if (!isDemoReqest){
            isDemoReqest = true;
            Demo7.subscribe2();
        }else {
            Demo7.request(1);
        }

    }

    public void demo7_3(View view) {
        Demo7.subscribe3();
    }

    private boolean isDemoReqest;
    public void demo7_4(View view) {
        if (!isDemoReqest){
            isDemoReqest = true;
            Demo7.subscribe4();
        }else {
            Demo7.request(1);
        }
    }

    public void demo7_5(View view) {
        if (!isDemoReqest){
            isDemoReqest = true;
            Demo7.subscribe5();
        }else {
            Demo7.request(1);
        }
    }

    public void demo8_1(View view) {
        Demo8.subscribe1();
    }

    public void demo8_2(View view) {
        Demo8.subscribe2();
    }
    public void demo8_3(View view) {
        Demo8.subscribe3();
    }
    public void demo8_4(View view) {
        Demo8.subscribe4();
    }

    public void demo8_3_request(View view) {
        Demo8.request(128);
    }


    public void demo9_1(View view) {
        Demo9.subscribe1();
    }

    public void demo9_request(View view) {

    }

    public void demo9_2(View view) {
    }

    public void demo9_3(View view) {
    }

    public void demo9_4(View view) {
        Demo9.subscribe4();
    }

    public void demo9_5(View view) {
        Demo9.subscribe5();
    }

    public void demo9_6(View view) {
        Demo9.subscribe6();
    }

    public void demo10_1(View view) {
        Demo10.okhttp();
    }

    public void demo10_2(View view) {
        Demo10.subscribe2();
    }

    public void demo10_3(View view) {
        Demo10.subscribe3();
    }

    public void demo10_4(View view) {
        Demo10.subscribe4();
    }

    public void demo10_5(View view) {
        Demo10.subscribe5();
    }

    public void demo10_6(View view) {
        Demo10.subscribe6();
    }

    public void demo10_7(View view) {
        Demo10.subscribe7();
    }

    public void demo10_8(View view) {
        Demo10.subscribe8();
    }

    public void demo10_9(View view) {
        Demo10.subscribe9();
    }

    public void demo10_10(View view) {
        Demo10.subscribe10();
    }

    public void demo10_11(View view) {
        Demo10.subscribe11();
    }

    public void demo10_12(View view) {
        Demo10.subscribe12();
    }

    public void demo10_13(View view) {
        Demo10.subscribe13();
    }

    public void demo10_14(View view) {
        Demo10.subscribe13();
    }

    public void demo10_15(View view) {
        Demo10.subscribe13();
    }

    public void demo10_16(View view) {
        Demo10.subscribe13();
    }
}
