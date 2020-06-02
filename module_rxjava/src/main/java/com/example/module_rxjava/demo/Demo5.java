package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(五)
 * 地址：https://www.jianshu.com/p/0f2d6c2387c9
 * 本章内容：Backpressure引入
 */
public class Demo5 {


    /**
     * 上一节中我们说到Zip可以将多个上游发送的事件组合起来发送给下游, 那大家有没有想过一个问题, 如果其中一个水管A发送事件特别快,
     * 而另一个水管B 发送事件特别慢, 那就可能出现这种情况, 发得快的水管A 已经发送了1000个事件了, 而发的慢的水管B 才发一个出来,
     * 组合了一个之后水管A 还剩999个事件, 这些事件需要继续等待水管B 发送事件出来组合, 那么这么多的事件是放在哪里的呢?
     * 总有一个地方保存吧? 没错, Zip给我们的每一根水管都弄了一个水缸 , 用来保存这些事件;
     */
    public static void subscribeZip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件                                                    
                    emitter.onNext(i);
                    Util.printLine("emitter:" + i);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Disposable disposable = Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Util.printLine(s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Util.printLine(throwable.toString());
                /**
                 * 内存占用以斜率为1的直线迅速上涨, 几秒钟就300多M , 最终报出了OOM:
                 */
            }
        });
    }

    /**
     * 这段代码很简单, 上游同样无限循环的发送事件, 在下游每次接收事件前延时2秒. 上下游工作在同一个线程里, 来看下运行结果:
     */
    public static void subscribe1() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
                    Util.printLine("emitter:" + i);
                }
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(1000);
                Util.printLine("" + integer);
                /**
                 * 为什么没有OOM呢, 因为上下游工作在同一个线程呀骚年们! 这个时候上游每次调用emitter.onNext(i)
                 * 其实就相当于直接调用了Consumer里的Thread.sleep(2000);
                 */
            }
        });
    }

    /**
     * 这段代码很简单, 上游同样无限循环的发送事件, 在下游每次接收事件前延时2秒. 上下游工作在同一个线程里, 来看下运行结果:
     */
    public static void subscribe2() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {   //无限循环发事件
                            emitter.onNext(i);
                            Util.printLine("emitter:" + i + ",thread:" + Thread.currentThread().getName());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(1000);
                        Util.printLine("accept：" + integer + ",thread:" + Thread.currentThread().getName());
                        /**
                         * 可以看到, 给上游加了个线程之后, 它就像脱缰的野马一样, 内存又爆掉了
                         * 为什么不加线程和加上线程区别这么大呢, 这就涉及了同步和异步的知识了.
                         * 当上下游工作在同一个线程中时, 这时候是一个同步的订阅关系, 也就是说上游每发送一个事件必须等
                         * 到下游接收处理完了以后才能接着发送下一个事件.
                         * 当上下游工作在不同的线程中时, 这时候是一个异步的订阅关系, 这个时候上游发送数据不需要等待下
                         * 游接收, 为什么呢, 因为两个线程并不能直接进行通信, 因此上游发送的事件并不能直接到下游里去,
                         * 这个时候就需要一个田螺姑娘来帮助它们俩, 这个田螺姑娘就是我们刚才说的水缸 ! 上游把事件发送到
                         * 水缸里去, 下游从水缸里取出事件来处理, 因此, 当上游发事件的速度太快, 下游取事件的速度太慢,
                         * 水缸就会迅速装满, 然后溢出来, 最后就OOM了.
                         *
                         */
                    }
                });
    }

/**
 * 从图中我们可以看出, 同步和异步的区别仅仅在于是否有水缸.
 * 相信通过这个例子大家对线程之间的通信也有了比较清楚的认知和理解.
 * 源头找到了, 只要有水缸, 就会出现上下游发送事件速度不平衡的情况, 因此当我们以后遇到这种情况时, 仔细思考一下水缸在哪里,
 * 找到水缸, 你就找到了解决问题的办法.
 *
 */


}
