package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 参考文章：给初学者的RxJava2.0教程(三)
 * 地址：https://www.jianshu.com/p/128e662906af
 * 本章内容：map操作符的基本使用
 */
class Demo3 {


    /**
     * Map操作符的使用：
     * map是RxJava中最简单的一个变换操作符了, 它的作用就是对上游发送的每一个事件应用一个函数,
     * 使得每一个事件都按照指定的函数去变化.
     */
    static void subscribeMap() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
                Util.printLine("emit 4");
                emitter.onNext(4);
            }
        }).map(new Function<Integer, String>() {
            /**
             * 将int类型转换成了string类型并继续向下传递
             */
            @Override
            public String apply(Integer integer) throws Exception {
                Util.printLine( "Map Function: " + integer);
                return "This is new result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Util.printLine(s);
                /**
                 * 运行结果：
                 * Demo: emit 1
                 * Demo: Map Function: 1
                 * Demo: This is new result 1
                 * Demo: emit 2
                 * Demo: Map Function: 2
                 * Demo: This is new result 2
                 * Demo: emit 3
                 * Demo: Map Function: 3
                 * Demo: This is new result 3
                 * Demo: emit complete
                 * Demo: emit 4
                 */
            }
        });
    }

    /**
     * flatMap操作符的使用：
     * flatMap是一个非常强大的操作符, 先用一个比较难懂的概念说明一下:
     * FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
     * 简单来说：上游每发送一个事件, flatMap都将创建一个新的水管, 然后发送转换之后的新的事件,
     * 下游接收到的就是这些新的水管发送的数据. 这里需要注意的是, flatMap并不保证事件的顺序,
     */
    static void subscribeFlatMap() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
                Util.printLine("emit 4");
                emitter.onNext(4);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Util.printLine( "FlatMap Function: " + integer);
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer + "::" +i);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Util.printLine(s);
                /**
                 * 运行结果：
                 * Demo: emit 1
                 * Demo: FlatMap Function: 1
                 * Demo: emit 2
                 * Demo: FlatMap Function: 2
                 * Demo: I am value 1::0
                 * Demo: I am value 1::1
                 * Demo: I am value 1::2
                 * Demo: emit 3
                 * Demo: FlatMap Function: 3
                 * Demo: I am value 2::0
                 * Demo: emit complete
                 * Demo: emit 4
                 */
            }
        });
    }

    /**
     * concatMap操作符的使用：
     * 它和flatMap的作用几乎一模一样, 只是它的结果是严格按照上游发送的顺序来发送的, 来看个代码吧
     */
    static void subscribeConcatMap() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
                Util.printLine("emit 4");
                emitter.onNext(4);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Util.printLine( "concatMap Function: " + integer);
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer + "::" +i);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Util.printLine(s);
                /**
                 * 运行结果：
                 * Demo: emit 1
                 * Demo: FlatMap Function: 1
                 * Demo: emit 2
                 * Demo: FlatMap Function: 2
                 * Demo: emit 3
                 * Demo: FlatMap Function: 3
                 * Demo: emit complete
                 * Demo: emit 4
                 * Demo: I am value 1::0
                 * Demo: I am value 1::1
                 * Demo: I am value 1::2
                 * Demo: I am value 2::0
                 * Demo: I am value 2::1
                 * Demo: I am value 2::2
                 * Demo: I am value 3::0
                 * Demo: I am value 3::1
                 * Demo: I am value 3::2
                 */
            }
        });
    }

    // TODO: 2020/5/27 还差实际使用案例，待温习retrofit后再完成

}
