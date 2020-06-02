package com.example.module_rxjava.demo;

import com.example.module_rxjava.utils.Util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考文章：给初学者的RxJava2.0教程(四)
 * 地址：https://www.jianshu.com/p/bb58571cdb64
 * 本章内容：介绍zip这个操作符的使用
 */
public class Demo4 {

    /**
     * zip操作符介绍：
     * 1.组合的过程是分别从两个observable里各取出一个事件来进行组合, 并且一个事件只能被使用一次, 组合的顺序是严格按照事件发送的
     * 顺利来进行的, 也就是说不会出现圆形1 事件和三角形B 事件进行合并, 也不可能出现圆形2 和三角形A 进行合并的情况.
     * 2.最终下游收到的事件数量 是和上游中发送事件最少的那一根水管的事件数量 相同. 这个也很好理解, 因为是从每一根水管 里取
     * 一个事件来进行合并, 最少的 那个肯定就最先取完 , 这个时候其他的水管尽管还有事件 , 但是已经没有足够的事件来组合了,
     * 因此下游就不会收到剩余的事件了.
     */
    public static void subscribeZip() {
        //第一个observable
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);
                Util.printLine("emit 4");
                emitter.onNext(4);
                Thread.sleep(1000);
                Util.printLine("emit complete1");
                emitter.onComplete();
            }
        });
        //第二个observable
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Util.printLine("emit A");
                emitter.onNext("A");
                Util.printLine("emit B");
                emitter.onNext("B");
                Util.printLine("emit C");
                emitter.onNext("C");
                Util.printLine("emit complete2");
                emitter.onComplete();
            }
        });
        //组合
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + "：" + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Util.printLine("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Util.printLine("onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Util.printLine("onError");
            }

            @Override
            public void onComplete() {
                Util.printLine("onComplete");
            }
        });
        /**
         * 执行结果:
         * Demo: onSubscribe
         * Demo: emit 1
         * Demo: emit 2
         * Demo: emit 3
         * Demo: emit 4
         * Demo: emit complete1
         * Demo: emit A
         * Demo: onNext: 1：A
         * Demo: emit B
         * Demo: onNext: 2：B
         * Demo: emit C
         * Demo: onNext: 3：C
         * Demo: emit complete2
         * Demo: onComplete
         *
         * 尽管给observable1增加了延时，但是依然是observable1优先一次性发送完所有的emit,为什么会有这种情况呢?
         * 因为我们两根水管都是运行在同一个线程里, 同一个线程里执行代码肯定有先后顺序呀.
         */
    }

    /**
     * 因此我们来稍微改一下, 不让他们在同一个线程
     */
    public static void subscribeZipIO() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);

                Util.printLine("emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Util.printLine("emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);

                Util.printLine("emit 4");
                emitter.onNext(4);
                Thread.sleep(1000);

                Util.printLine("emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Util.printLine("emit A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Util.printLine("emit B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Util.printLine("emit C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Util.printLine("emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Util.printLine("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Util.printLine("onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Util.printLine("onError");
            }

            @Override
            public void onComplete() {
                Util.printLine("onComplete");
            }
        });
        /**
         * 输出结果：
         * Demo: onSubscribe
         * Demo: emit 1
         * Demo: emit A
         * Demo: onNext: 1A
         * Demo: emit B
         * Demo: emit 2
         * Demo: onNext: 2B
         * Demo: emit 3
         * Demo: emit C
         * Demo: onNext: 3C
         * Demo: emit 4
         * Demo: emit complete2
         * Demo: onComplete
         * Demo: emit complete1
         */
    }


}
