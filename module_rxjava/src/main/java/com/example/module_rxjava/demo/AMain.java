package com.example.module_rxjava.demo;

/**
 * 执行入口
 */
public class AMain {
    public static void main(String[] args) {
        testDemo1(1);
    }

    private static void testDemo1(int code) {
        switch (code) {
            case 0:
                //正常调用方法
                Demo1 demo11 = new Demo1();
                demo11.subscribe();
                break;
            case 1:
                //rxjava链式调用
                Demo1.subscribeSimple();
                break;
            case 2:
                //rxjava不同线程
                Demo2.subscribe();
                break;
            case 3:
                //rxjava操作符Map的使用
                Demo3.subscribeMap();
                break;
            case 4:
                //rxjava操作符flatMap的使用
                Demo3.subscribeFlatMap();
                break;
            case 5:
                //rxjava操作符zip的使用
                Demo4.subscribeZip();
                break;
            case 6:
                //rxjava操作符zip的异步使用
                Demo4.subscribeZipIO();
                break;



        }
    }
}
