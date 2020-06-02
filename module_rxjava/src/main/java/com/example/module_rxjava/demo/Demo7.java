package com.example.module_rxjava.demo;

import android.net.wifi.aware.SubscribeConfig;

import com.example.module_rxjava.utils.Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(七)
 * 地址：https://www.jianshu.com/p/9b1304435564
 * 本章内容：上游变成了Flowable, 下游变成了Subscriber的基本使用场景介绍
 */
public class Demo7 {


    /**
     * 上一节里我们学习了只使用Observable如何去解决上下游流速不均衡的问题, 之所以学习这个是因为Observable还是有很多它
     * 使用的场景, 有些朋友自从听说了Flowable之后就觉得Flowable能解决任何问题, 甚至有抛弃Observable这种想法,
     * 这是万万不可的, 它们都有各自的优势和不足.
     * <p>
     * 先来看个简单的例子
     */
    public static void subscribe1() {
        Flowable<Integer> integerFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);//多了一个参数
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Util.printLine( "onSubscribe");
                s.request(Long.MAX_VALUE);  //注意这句代码
            }

            @Override
            public void onNext(Integer integer) {
                Util.printLine( "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Util.printLine( "onError: " + t);
            }

            @Override
            public void onComplete() {
                Util.printLine( "onComplete");
            }
        };
        integerFlowable.subscribe(subscriber);
        /**
         *
         * Demo: onSubscribe
         * Demo: emit 1
         * Demo: onNext: 1
         * Demo: emit 2
         * Demo: onNext: 2
         * Demo: emit 3
         * Demo: onNext: 3
         * Demo: emit complete
         * Demo: onComplete
         * 结果也和我们预期的是一样的.
         *
         * 我们注意到这次和Observable有些不同. 首先是创建Flowable的时候增加了一个参数, 这个参数是用来选择背压,
         * 也就是出现上下游流速不均衡的时候应该怎么处理的办法, 这里我们直接用BackpressureStrategy.ERROR这种方式,
         * 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,这个异常就是著名的MissingBackpressureException.
         * 其余的策略后面再来讲解.
         */
    }

    /**
     * 另外的一个区别是在下游的onSubscribe方法中传给我们的不再是Disposable了, 而是Subscription, 它俩有什么区别呢,
     * 首先它们都是上下游中间的一个开关, 之前我们说调用Disposable.dispose()方法可以切断水管, 同样的调用
     * Subscription.cancel()也可以切断水管, 不同的地方在于Subscription增加了一个void request(long n)方法,
     * 这个方法有什么用呢, 在上面的代码中也有这么一句代码: s.request(Long.MAX_VALUE);
     * 这句代码有什么用呢, 不要它可以吗? 我们来试试:
     */
    public static void subscribe2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1"+",requested:"+ emitter.requested());
                emitter.onNext(1);
                Util.printLine("emit 2"+",requested:"+ emitter.requested());
                emitter.onNext(2);
                Util.printLine("emit 3"+",requested:"+ emitter.requested());
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Util.printLine( "onSubscribe");
                subscription = s;
                subscription.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                Util.printLine( "onNext: " + integer);
//                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                Util.printLine( "onError: " + t);
            }

            @Override
            public void onComplete() {
                Util.printLine( "onComplete");
            }
        });
        /**
         * onSubscribe
         * emit 1
         * onError: io.reactivex.exceptions.MissingBackpressureException:
         * create: could not emit value due to lack of requests
         * emit 2
         * emit 3
         * emit complete
         * 从运行结果中可以看到, 在上游发送第一个事件之后, 下游就抛出了一个著名的MissingBackpressureException异常,
         * 并且下游没有收到任何其余的事件. 可是这是一个同步的订阅呀, 上下游工作在同一个线程, 上游每发送一个事件应该会等待
         * 下游处理完了才会继续发事件啊, 不可能出现上下游流速不均衡的问题呀.
         *带着这个疑问, 我们再来看看异步的情况:
         */
    }

    /**
     * 异步:
     */
    public static void subscribe3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Util.printLine( "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Util.printLine( "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Util.printLine( "onError: " + t);
            }

            @Override
            public void onComplete() {
                Util.printLine( "onComplete");
            }
        });

        /**
         * 运行结果：
         * Demo: onSubscribe
         * Demo: emit 1
         * Demo: emit 2
         * Demo: emit 3
         * Demo: emit complete
         * 这次上游正确的发送了所有的事件, 但是下游一个事件也没有收到. 这是因为什么呢?
         *
         * 这是因为Flowable在设计的时候采用了一种新的思路也就是响应式拉取的方式来更好的解决上下游流速不均衡的问题,
         * 与我们之前所讲的控制数量和控制速度不太一样, 这种方式用通俗易懂的话来说就好比是叶问打鬼子, 我们把上游看成小日本,
         * 把下游当作叶问, 当调用Subscription.request(1)时, 叶问就说我要打一个! 然后小日本就拿出一个鬼子给叶问,
         * 让他打, 等叶问打死这个鬼子之后, 再次调用request(10), 叶问就又说我要打十个! 然后小日本又派出十个鬼子给叶问,
         * 然后就在边上看热闹, 看叶问能不能打死十个鬼子, 等叶问打死十个鬼子后再继续要鬼子接着打...
         *
         * 学习了request, 我们就可以解释上面的两段代码了.
         *
         * 首先第一个同步的代码, 为什么上游发送第一个事件后下游就抛出了MissingBackpressureException异常,
         * 这是因为下游没有调用request, 上游就认为下游没有处理事件的能力, 而这又是一个同步的订阅, 既然下游处理不了,
         * 那上游不可能一直等待吧, 如果是这样, 万一这两根水管工作在主线程里, 界面不就卡死了吗, 因此只能抛个异常来提醒我们.
         * 那如何解决这种情况呢, 很简单啦, 下游直接调用request(Long.MAX_VALUE)就行了, 或者根据上游发送事件的数量来
         * request就行了, 比如这里request(3)就可以了.
         *
         */
    }

    /**
     * 然后我们再来看看第二段代码, 为什么上下游没有工作在同一个线程时, 上游却正确的发送了所有的事件呢? 这是因为在Flowable
     * 里默认有一个大小为128的水缸, 当上下游工作在不同的线程中时, 上游就会先把事件发送到这个水缸中, 因此, 下游虽然没有调
     * 用request, 但是上游在水缸中保存着这些事件, 只有当下游调用request时, 才从水缸里取出事件发给下游.
     *
     * 是不是这样呢, 我们来验证一下:
     *
     */
    public static Subscription subscription;
    public static void request(int i){
        subscription.request(i);
    }
    public static void subscribe4() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                Util.printLine( "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Util.printLine( "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Util.printLine( "onError: " + t);
            }

            @Override
            public void onComplete() {
                Util.printLine( "onComplete");
            }
        });
        /**
         * 结果似乎像那么回事, 上游发送了3个事件保存到了水缸里, 下游每request一个, 就接收一个进行处理.
         */
    }

    /**
     * 刚刚我们有说到水缸的大小为128, 有朋友就问了, 你说128就128吗,我不信. 那就来验证一下
     * 这里我们让上游一次性发送了128个事件, 下游一个也不接收, 来看看运行结果:
     */
    public static void subscribe5() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 129; i++) {
                    Util.printLine("emit " + i +",requested:"+ emitter.requested());
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
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
         *这段代码的运行结果很正常, 没有任何错误和异常, 上游仅仅是发送了128个事件.
         *
         * 那来试试129个呢, 把上面代码中的128改成129试试:
         * 这次可以看到, 在上游发送了第129个事件的时候, 就抛出了MissingBackpressureException异常, 提醒我们发洪水啦.
         * 当然了, 这个128也不是我凭空捏造出来的, Flowable的源码中就有这个buffersize的大小定义, 可以自行查看.
         *
         * 注意这里我们是把上游发送的事件全部都存进了水缸里, 下游一个也没有消费, 所以就溢出了, 如果下游去消费了事件,
         * 可能就不会导致水缸溢出来了. 这里我们说的是可能不会, 这也很好理解, 比如刚才这个例子上游发了129个事件,
         * 下游只要快速的消费了一个事件, 就不会溢出了, 但如果下游过了十秒钟再来消费一个, 那肯定早就溢出了.
         *
         */
    }

}
