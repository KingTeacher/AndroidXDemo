package com.example.module_degger.graph;

import com.example.module_degger.bean.UserRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
// @Component使Dagger创建依赖关系图
@Component
public
interface ApplicationGraph {

    // 组件接口内部函数的返回类型为可以从图中获得
    UserRepository userRepository();
}
