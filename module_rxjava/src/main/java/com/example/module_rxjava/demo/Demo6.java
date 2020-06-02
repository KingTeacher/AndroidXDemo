package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(六)
 * 地址：https://www.jianshu.com/p/e4c6d7989356
 * 本章内容：手动实现异步时减缓发送速率,引出Flowable
 */
public class Demo6 {


    /**
     * 在上一节中, 我们找到了上下游流速不均衡的源头 , 在这一节里我们将学习如何去治理它 . 可能很多看过其他人写的文章的朋
     * 友都会觉得只有Flowable才能解决 , 所以大家对这个Flowable都抱有很大的期许 , 其实呐 , 你们毕竟图样图森破 ,
     * 今天我们先抛开Flowable, 仅仅依靠我们自己的双手和智慧 , 来看看我们如何去治理 , 通过本节的学习之后我们再来看Flowable,
     * 你会发现它其实并没有想象中那么牛叉, 它只是被其他人过度神化了
     * <p>
     * 之前我们说了, 上游发送的所有事件都放到水缸里了, 所以瞬间水缸就满了, 那我们可以只放我们需要的事件到水缸里呀,
     * 只放一部分数据到水缸里, 这样不就不会溢出来了吗, 因此, 我们把上节的代码修改一下:
     */
    public static void subscribe1() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        //在这段代码中我们增加了一个filter, 只允许能被10整除的事件通过, 再来看看运行结果:
                        return integer % 10 == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("" + integer);
                        /**
                         * 可以看到, 通过减少进入水缸的事件数量的确可以缓解上下游流速不均衡的问题,
                         * 但是力度还不够
                         */
                    }
                });
    }

    /**
     * 我们再来看一段代码:
     * 这里用了一个sample操作符, 简单做个介绍, 这个操作符每隔指定的时间就从上游中取出一个事件发送给下游.
     * 这里我们让它每隔2秒取一个事件给下游, 来看看这次的运行结果吧:
     */
    public static void subscribe2() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {
                            emitter.onNext(i);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)  //sample取样
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("" + integer);
                        /**
                         * 这次我们可以看到, 虽然上游仍然一直在不停的发事件, 但是我们只是每隔一定时间取一个放进水缸里,
                         * 并没有全部放进水缸里, 因此这次内存仅仅只占用了5M.
                         */
                    }
                });
    }

    /**
     * 前面这两种方法归根到底其实就是减少放进水缸的事件的数量, 是以数量取胜, 但是这个方法有个缺点, 就是丢失了大部分的事件.
     * 那么我们换一个角度来思考, 既然上游发送事件的速度太快, 那我们就适当减慢发送事件的速度, 从速度上取胜, 听上去不错, 我们来试试:
     */
    public static void subscribe3() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                    Thread.sleep(2000);  //每次发送完事件延时2秒
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine( "" + integer);
                        /**
                         * 可以看到, 我们给上游加上延时了之后, 瞬间一头发情的公牛就变得跟只小绵羊一样, 如此温顺,
                         * 如此平静, 如此平稳的内存线, 美妙极了. 而且事件也没有丢失, 上游通过适当的延时, 不但减
                         * 缓了事件进入水缸的速度, 也可以让下游有充足的时间从水缸里取出事件来处理 , 这样一来,
                         * 就不至于导致大量的事件涌进水缸, 也就不会OOM啦.
                         *
                         */
                    }
                });
    }

    /**
     * 因此我们总结一下, 本节中的治理的办法就两种:
     *
     * 一是从数量上进行治理, 减少发送进水缸里的事件
     * 二是从速度上进行治理, 减缓事件发送进水缸的速度
     */

}
