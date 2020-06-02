package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(八)
 * 地址：https://www.jianshu.com/p/a75ecf461e02
 * 本章内容：BackpressureStrategy的几种使用
 */
public class Demo8 {
    public static Subscription subscription;
    public static void request(int i){
        subscription.request(i);
    }

    /**
     * 在上一节中最后我们有个例子, 当上游一次性发送128个事件的时候是没有任何问题的, 一旦超过128就会抛出
     * MissingBackpressureException异常, 提示你上游发太多事件了, 下游处理不过来, 那么怎么去解决呢?
     *
     * 我们先来思考一下, 发送128个事件没有问题是因为FLowable内部有一个大小为128的水缸, 超过128就会装满溢出来,
     * 那既然你水缸这么小, 那我给你换一个大水缸如何, 听上去很有道理的样子, 来试试:
     *
     * 这次我们直接让上游发送了1000个事件,下游仍然不调用request去请求, 与之前不同的是, 这次我们用的策略是
     * BackpressureStrategy.BUFFER, 这就是我们的新水缸啦, 这个水缸就比原来的水缸牛逼多了,如果说原来的水缸是95式步枪,
     * 那这个新的水缸就好比黄金AK , 它没有大小限制, 因此可以存放许许多多的事件.
     *
     */
    public static void subscribe1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Util.printLine("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER)//更换了策略
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                });
        /**
         * 我们会发现所有的数据都可以发送成功，不会报错，这时的FLowable表现出来的特性的确和Observable一模一样,
         * 因此, 如果你像这样单纯的使用FLowable, 同样需要注意OOM的问题
         */
    }

    /**
     * 另外FLowable中也有其他方法, 对应的就是BackpressureStrategy.DROP和BackpressureStrategy.LATEST这两种策略.
     * BackpressureStrategy.DROP：
     */
    public static void subscribe2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Util.printLine("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP)//更换了策略
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                });
    }

    /**
     * BackpressureStrategy.LATEST:
     */
    public static void subscribe3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Util.printLine("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST)//更换了策略
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                });
    }

    /**
     * 这些FLowable是我自己创建的, 所以我可以选择策略, 那面对有些FLowable并不是我自己创建的, 该怎么办呢?
     * 比如RxJava中的interval操作符, 这个操作符并不是我们自己创建的, 来看下面这个例子吧:
     * interval操作符发送Long型的事件, 从0开始, 每隔指定的时间就把数字加1并发送出来, 在这个例子里,
     * 我们让它每隔1毫秒就发送一次事件, 在下游延时1秒去接收处理
     *一运行就抛出了MissingBackpressureException异常, 提醒我们发太多了, 那么怎么办呢, 这个又不是我们自己创建的FLowable啊..
     * onBackpressureBuffer()
     * onBackpressureDrop()
     * onBackpressureLatest()
     * 熟悉吗? 这跟我们上面学的策略是一样的, 用法也简单, 拿刚才的例子现学现用:
     *
     */

    public static void subscribe4() {
        Flowable.interval(1, TimeUnit.MICROSECONDS)
                .onBackpressureDrop()  //加上背压策略，注销调改行代码会抛出了MissingBackpressureException异常
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        subscription = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Util.printLine("onNext: " + aLong);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                });
    }


}
