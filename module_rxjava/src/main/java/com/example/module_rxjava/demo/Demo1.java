package com.example.module_rxjava.demo;

import android.content.Intent;
import android.util.Log;

import com.example.module_rxjava.utils.Util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 参考文章：给初学者的RxJava2.0教程(一)
 * 地址：https://www.jianshu.com/p/464fa025229e
 * 本章内容：介绍简单的调用方法和Emitter与Disposable.dispose()方法的使用，subscribe方法重载的使用
 */
class Demo1 {


    //创建一个上游 Observable：
    private Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        }
    });

    //创建一个下游 Observer
    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            Util.printLine("onSubscribe");
        }

        @Override
        public void onNext(Integer value) {
            Util.printLine("onNext: " + value);
        }

        @Override
        public void onError(Throwable e) {
            Util.printLine("error: ");
        }

        @Override
        public void onComplete() {
            Util.printLine("onComplete");
        }
    };

    /**
     * 注意: 只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
     */
    void subscribe() {
        observable.subscribe(observer);
    }

    /**
     * 把这段代码连起来写就成了RxJava引以为傲的链式操作：
     */
    public static void subscribeSimple() {
        /**
         * ObservableEmitter： Emitter是发射器的意思，那就很好猜了，这个就是用来发出事件的，它可以发出三种类型的事件，
         * 通过调用emitter的onNext(T value)、onComplete()和onError(Throwable error)就可以分别发出next事件、complete事件和error事件。
         * 但是，请注意，并不意味着你可以随意乱七八糟发射事件，需要满足一定的规则：
         *
         * 1.上游可以发送无限个onNext, 下游也可以接收无限个onNext.
         * 2.当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
         * 3.当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
         * 4.上游可以不发送onComplete或onError.
         * 5.最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
         *
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
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
        }).subscribe(new Observer<Integer>() {
            /**当调用它的Disposable.dispose()方法时, 它就会将两根管道切断, 从而导致下游收不到事件.
             * 调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.*/
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Util.printLine("onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                Util.printLine("onNext: " + value+"，disposable："+disposable.isDisposed());

                if (value == 2){
                    disposable.dispose();
                    Util.printLine("disposable："+disposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Util.printLine("error: ");
            }

            @Override
            public void onComplete() {
                Util.printLine("onComplete");
            }
        });
    }

    /**
     * subscribe()有多个重载的方法:
     * public final Disposable subscribe() {}
     *     public final Disposable subscribe(Consumer<? super T> onNext) {}
     *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {}
     *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
     *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
     *     public final void subscribe(Observer<? super T> observer) {}
     *
     * 1.不带任何参数的subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可不管你发什么.
     * 2.带有一个Consumer参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见, 因此我们如果只需要onNext事件可以这么写:
     *
     */
    static void subscribeSimple2(){
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
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Util.printLine("accept: "+integer);
                /**
                 * 执行结果如下
                 * Demo: emit 1
                 * Demo: accept: 1
                 * Demo: emit 2
                 * Demo: accept: 2
                 * Demo: emit 3
                 * Demo: accept: 3
                 * Demo: emit complete
                 * Demo: emit 4
                 */
            }
        });
    }

}
