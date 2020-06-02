package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(九)
 * 地址：https://www.jianshu.com/p/36e0f7f43a51
 * 本章内容：FlowableEmitter接口中requested()方法的使用
 */
public class Demo9 {
    public static Subscription subscription;

    public static void request(int i) {
        subscription.request(i);
    }

    /**
     * 下游中调用Subscription.request(n)就可以告诉上游，下游能够处理多少个事件，那么上游要根据下游的处理能力正确的去发
     * 送事件，那么上游是不是应该知道下游的处理能力是多少啊，对吧，不然，一个巴掌拍不响啊，这种事情得你情我愿才行。
     * <p>
     * 那么上游从哪里得知下游的处理能力呢？我们来看看上游最重要的部分，肯定就是FlowableEmitter了啊，我们就是通过它来发送事件的啊
     * <p>
     * 先来看同步的情况吧：
     * 这个例子中，我们在上游中打印出当前的request数量，下游什么也不做
     */
    public static void subscribe1() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine("current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
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
                        Util.printLine("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                    /**
                     * current requested: 0
                     */
                });
    }

    /**
     * 那下游要是调用了request()呢，来看
     */
    public static void subscribe2() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine("current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        s.request(10);
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                    /**
                     * current requested: 10
                     */
                });
    }

    /**
     * 上游的requested的确是根据下游的请求来决定的，那要是下游多次请求呢？比如这样
     */
    public static void subscribe3() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine("current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine("onSubscribe");
                        s.request(10);
                        s.request(100);
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine("onComplete");
                    }
                    /**
                     * current requested: 110
                     * 看来多次调用也没问题，做了加法。
                     *
                     * 诶加法？对哦，只是做加法，那什么时候做减法呢？
                     * 当然是发送事件啦！
                     */
                });
    }

    /**
     * 来看个例子吧：
     */
    public static void subscribe4() {
            Flowable
                    .create(new FlowableOnSubscribe<Integer>() {
                        @Override
                        public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                            Util.printLine("before emit, requested = " + emitter.requested());

                            Util.printLine("emit 1");
                            emitter.onNext(1);
                            Util.printLine("after emit 1, requested = " + emitter.requested());

                            Util.printLine("emit 2");
                            emitter.onNext(2);
                            Util.printLine("after emit 2, requested = " + emitter.requested());

                            Util.printLine("emit 3");
                            emitter.onNext(3);
                            Util.printLine("after emit 3, requested = " + emitter.requested());

                            Util.printLine("emit complete");
                            emitter.onComplete();

                            Util.printLine("after emit complete, requested = " + emitter.requested());
                        }
                    }, BackpressureStrategy.ERROR)
                    .subscribe(new Subscriber<Integer>() {

                        @Override
                        public void onSubscribe(Subscription s) {
                            Util.printLine("onSubscribe");
                            subscription = s;
                            s.request(10);  //request 10
                        }

                        @Override
                        public void onNext(Integer integer) {
                            Util.printLine("onNext: " + integer);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Util.printLine( "onError: "+ t);
                        }

                        @Override
                        public void onComplete() {
                            Util.printLine("onComplete");
                        }
                    });
        /**
         * Demo: onSubscribe
         * Demo: before emit, requested = 10
         * Demo: emit 1
         * Demo: onNext: 1
         * Demo: after emit 1, requested = 9
         * Demo: emit 2
         * Demo: onNext: 2
         * Demo: after emit 2, requested = 8
         * Demo: emit 3
         * Demo: onNext: 3
         * Demo: after emit 3, requested = 7
         * Demo: emit complete
         * Demo: onComplete
         * Demo: after emit complete, requested = 7
         *
         * 大家应该能看出端倪了吧，下游调用request(n) 告诉上游它的处理能力，上游每发送一个next事件之后，
         * requested就减一，注意是next事件，complete和error事件不会消耗requested，当减到0时，则代表下游没有处理
         * 能力了，这个时候你如果继续发送事件，会发生什么后果呢？当然是MissingBackpressureException啦
         *
         */
    }

    /**
     * 再来说说异步的情况，异步和同步会有区别吗？
     */
    public static void subscribe5() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine( "current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine( "onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine( "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine( "onComplete");
                    }
                });
        /**
         * 这次是异步的情况，上游啥也不做，下游也啥也不做，来看看运行结果：
         * Demo: onSubscribe
         * Demo: current requested: 128
         */
    }
    /**
     * 再来请求1000次，看看结果
     */
    public static void subscribe6() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine( "current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Util.printLine( "onSubscribe");
                        subscription = s;
                        s.request(1000);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Util.printLine( "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Util.printLine("onError: "+ t);
                    }

                    @Override
                    public void onComplete() {
                        Util.printLine( "onComplete");
                    }
                });
        /**
         * 这次我们在下游调用了request（1000）告诉上游我要打1000个，按照之前我们说的，这次的运行结果应该是1000，来看看运行结果：
         *
         * Demo: onSubscribe
         * Demo: current requested: 128
         *
         * 当上下游工作在不同的线程里时，每一个线程里都有一个requested，而我们调用request（1000）时，实际上改变的是下游主线
         * 程中的requested，而上游中的requested的值是由RxJava内部调用request(n)去设置的，这个调用会在合适的时候自动触发。
         * 现在我们就能理解为什么没有调用request，上游中的值是128了，因为下游在一开始就在内部调用了request(128)去设置了上游
         * 中的值，因此即使下游没有调用request()，上游也能发送128个事件，这也可以解释之前我们为什么说Flowable中默认的水缸大
         * 小是128，其实就是这里设置的。
         *
         *
         */
    }

    /**
     * 刚刚说了，设置上游requested的值的这个内部调用会在合适的时候自动触发，那到底什么时候是合适的时候呢？
     * 是发完128个事件才去调用吗？还是发送了一半才去调用呢？
     */
    public static void subscribe7() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Util.printLine("First requested = " + emitter.requested());
                        boolean flag;
                        for (int i = 0; ; i++) {
                            flag = false;
                            while (emitter.requested() == 0) {
                                if (!flag) {
                                    Util.printLine("Oh no! I can't emit value!");
                                    flag = true;
                                }
                            }
                            emitter.onNext(i);
                            Util.printLine("emit " + i + " , requested = " + emitter.requested());
                        }
                    }
                }, BackpressureStrategy.ERROR)
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
         * /TAG: onSubscribe
         * D/TAG: First requested = 128
         * D/TAG: emit 0 , requested = 127
         * D/TAG: emit 1 , requested = 126
         * D/TAG: emit 2 , requested = 125
         *   ...
         * D/TAG: emit 124 , requested = 3
         * D/TAG: emit 125 , requested = 2
         * D/TAG: emit 126 , requested = 1
         * D/TAG: emit 127 , requested = 0
         * D/TAG: Oh no! I can't emit value!
         *
         * 首先运行之后上游便会发送完128个事件，之后便不做任何事情，从打印的结果中我们也可以看出这一点。
         *
         * 然后我们调用request(96)，这会让下游去消费96个事件，来看看运行结果吧：
         *
         * D/TAG: onNext: 0
         * D/TAG: onNext: 1
         *   ...
         * D/TAG: onNext: 92
         * D/TAG: onNext: 93
         * D/TAG: onNext: 94
         * D/TAG: onNext: 95
         * D/TAG: emit 128 , requested = 95
         * D/TAG: emit 129 , requested = 94
         * D/TAG: emit 130 , requested = 93
         * D/TAG: emit 131 , requested = 92
         *   ...
         * D/TAG: emit 219 , requested = 4
         * D/TAG: emit 220 , requested = 3
         * D/TAG: emit 221 , requested = 2
         * D/TAG: emit 222 , requested = 1
         * D/TAG: emit 223 , requested = 0
         * D/TAG: Oh no! I can't emit value!
         *
         * 可以看到，当下游消费掉第96个事件之后，上游又开始发事件了，而且可以看到当前上游的requested的值是96
         * (打印出来的95是已经发送了一个事件减一之后的值)，最终发出了第223个事件之后又进入了等待区，而223-127 正好等于 96。
         *
         * 这是不是说明当下游每消费96个事件便会自动触发内部的request()去设置上游的requested的值啊！没错，就是这样，而这个新的值就是96。
         *
         * 朋友们可以手动试试请求95个事件，上游是不会继续发送事件的。
         *
         */
    }

    /**
     * 实践：
     * 这个例子是读取一个文本文件，需要一行一行读取，然后处理并输出，如果文本文件很大的时候，比如几十M的时候，全部先读入内
     * 存肯定不是明智的做法，因此我们可以一边读取一边处理
     */
    public static void subscribe8() {
        Flowable
                .create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                        try {
                            FileReader reader = new FileReader("test.txt");
                            BufferedReader br = new BufferedReader(reader);

                            String str;

                            while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                                while (emitter.requested() == 0) {
                                    if (emitter.isCancelled()) {
                                        break;
                                    }
                                }
                                emitter.onNext(str);
                            }

                            br.close();
                            reader.close();

                            emitter.onComplete();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String string) {
                        System.out.println(string);
                        try {
                            Thread.sleep(2000);
                            subscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
