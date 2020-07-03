package com.example.module_degger;

import android.app.Application;

import com.example.module_degger.graph.ApplicationGraph;
import com.example.module_degger.graph.DaggerApplicationGraph;

class MyApplication extends Application {
    // 引用整个应用程序中使用的应用程序图
    public ApplicationGraph appComponent = DaggerApplicationGraph.create();


}
