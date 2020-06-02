package com.example.module_rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(二)
 * 地址：https://www.jianshu.com/p/8818b98c44e2
 * 本章主要内容：介绍切换上游和下游线程的方法和常用的预设线程
 */
class Demo2 {

    //创建一个上游 Observable：
    private static Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        }
    });

    //创建一个下游 Observer
    private static Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("onSubscribe");
        }

        @Override
        public void onNext(Integer value) {
            System.out.println("onNext: " + value);
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("error");
        }

        @Override
        public void onComplete() {
            System.out.println("onComplete");
        }
    };

    /**
     * subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
     * 多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
     * 多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
     *
     * 在RxJava中, 已经内置了很多线程选项供我们选择, 例如:
     * 1.Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     * 2.Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     * 3.Schedulers.newThread() 代表一个常规的新线程
     * 4.AndroidSchedulers.mainThread() 代表Android的主线程
     * 这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项, 在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高.
     *
     */
    static void subscribe() {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
