package com.example.module_rxjava.demo;

import android.os.SystemClock;

import com.example.module_rxjava.utils.Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * 操作符篇
 * <p>
 * 本章内容：介绍各种操作符的简单使用
 */
public class Demo10 {

    /**
     * 操作符：create
     */
    public static void subscribe1() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("accept:" + integer);
                    }
                });
    }

    /**
     * 操作符：map
     */
    public static void subscribe2() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "new Str: " + integer;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Util.printLine("onNext:" + s);
                    }
                });
    }

    /**
     * 操作符：flatMap
     */
    public static void subscribe3() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        final List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("I am value " + integer + "--" + i);
                        }
                        return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                        //此处加了延时，为了显示其无序的效果
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Util.printLine("onNext: " + s);
                    }
                });
        /**
         * Demo: onNext: I am value 1--0
         * Demo: onNext: I am value 1--1
         * Demo: onNext: I am value 1--2
         * Demo: onNext: I am value 3--0
         * Demo: onNext: I am value 3--1
         * Demo: onNext: I am value 3--2
         * Demo: onNext: I am value 2--0
         * Demo: onNext: I am value 2--1
         * Demo: onNext: I am value 2--2
         */
    }

    /**
     * 操作符：ConcatMap
     */
    public static void subscribe4() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                    }
                })
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        final List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("I am value " + integer + "--" + i);
                        }
                        return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                        //此处加了延时，为了显示其无序的效果
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Util.printLine("onNext: " + s);
                    }
                });
        /**
         *  Demo: onNext: I am value 1--0
         * Demo: onNext: I am value 1--1
         * Demo: onNext: I am value 1--2
         * Demo: onNext: I am value 2--0
         * Demo: onNext: I am value 2--1
         * Demo: onNext: I am value 2--2
         * Demo: onNext: I am value 3--0
         * Demo: onNext: I am value 3--1
         * Demo: onNext: I am value 3--2
         */
    }

    /**
     * 操作符：Zip
     */
    public static void subscribe5() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Util.printLine("emit 1");
                emitter.onNext(1);
                Util.printLine("emit 2");
                emitter.onNext(2);
                Util.printLine("emit 3");
                emitter.onNext(3);
                Util.printLine("emit 4");
                emitter.onNext(4);
                Util.printLine("emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

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
        }).subscribeOn(Schedulers.io());

        Disposable disposable = Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Util.printLine("onNext: " + s);
                    }
                });
        /**
         * Demo: emit A
         * Demo: emit B
         * Demo: emit C
         * Demo: emit complete2
         * Demo: emit 1
         * Demo: emit 2
         * Demo: emit 3
         * Demo: emit 4
         * Demo: emit complete1
         * Demo: onNext: 1A
         * Demo: onNext: 2B
         * Demo: onNext: 3C
         */
    }

    /**
     * Concat：只是单纯的把两个发射器连接成一个发射器
     */
    public static void subscribe6() {
        Disposable disposable = Observable.concat(Observable.just(1, 2, 4), Observable.just(8, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Util.printLine("onNext : " + integer + "\n");
                    }
                });
        /**
         * Demo: concat : 1
         * Demo: concat : 2
         * Demo: concat : 4
         * Demo: concat : 8
         * Demo: concat : 5
         * Demo: concat : 6
         */
    }

    /**
     * distinct：简单的去重，参考：Demo10_7；
     */
    public static void subscribe7() {
        Disposable disposable = Observable.just(1, 2, 1, 2, 4)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("onNext : " + integer + "\n");
                    }
                });
        /**
         *
         */
    }

    /**
     * Filter:可以接受一个参数，让其过滤掉不符合我们条件的值
     */
    public static void subscribe8() {
        Disposable disposable = Observable.just(1, 2, 3, 4, 5, 6, 7)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("onNext : " + integer + "\n");
                    }
                });

    }

    /**
     * buffer:操作符接受两个参数，buffer(count,skip)，作用是将 Observable 中的数据按 skip (步长)
     * 分成最大不超过 count 的 buffer
     */
    public static void subscribe9() {
        Disposable disposable = Observable.just(1, 2, 3, 4, 5, 6, 7)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Util.printLine("onNext buffer size : " + integers.size());
                        for (Integer i : integers) {
                            Util.printLine("onNext buffer value :" + i + "");
                        }
                    }
                });

    }

    /**
     * timer:一个定时任务
     */
    public static void subscribe10() {
        Util.printLine("timer start : " + System.currentTimeMillis());
        Disposable disposable = Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Util.printLine("onNext timer :" + aLong + " at " + System.currentTimeMillis());
                    }
                });

    }

    /**
     * interval：用于间隔时间执行某个操作，其接受三个参数，分别是第一次发送延迟，间隔时间，时间单位
     */
    public static void subscribe11() {
        Util.printLine("interval start : " + System.currentTimeMillis());
        Disposable disposable = Observable.interval(3, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Util.printLine("onNext interval:" + aLong + " at " + System.currentTimeMillis());
                    }
                });

    }

    /**
     * doOnNext：让订阅者在接收到数据之前干点有意思的事情
     */
    public static void subscribe12() {
        Disposable disposable = Observable.just(1, 2, 4, 5)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("on Next :" + integer + "线程:" + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Util.printLine("onNext :" + integer + "线程:" + Thread.currentThread().getName());
                    }
                });

    }
    /**
     * doOnNext：让订阅者在接收到数据之前干点有意思的事情
     */
    public static void subscribe13() {

//        Observable
//                .create(new ObservableOnSubscribe<Integer>() {
//                    int i = 0;
//
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                        Util.printLine("emitter:" + 1+",time:"+System.currentTimeMillis());
//                        emitter.onNext(1);
//                        if (i < 1000) {
//                            i++;
//                            emitter.onError(new Exception());
//                        } else {
//                            Util.printLine("emitter:" + 3);
//                            emitter.onNext(3);
//                        }
//                    }
//                })
//                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
//                        Util.printLine("retryWhen:" );
//                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
//                            @Override
//                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
//                                Util.printLine("retryWhen:" );
//                                return Observable.timer(1000,TimeUnit.MINUTES);
//                            }
//                        });
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Util.printLine("Disposable");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Util.printLine("onNext:" + integer+",time:"+System.currentTimeMillis());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Util.printLine("onError:" + e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Util.printLine("onComplete:");
//                    }
//                });

    }

    public static void subscribe21() {
        Single.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public static void okhttp() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Util.printLine("11map 线程:" + Thread.currentThread().getName() + "\n");
                e.onNext(11);
                Util.printLine("22map 线程:" + Thread.currentThread().getName() + "\n");
                e.onNext(22);
                Util.printLine("33map 线程:" + Thread.currentThread().getName() + "\n");
                e.onNext(33);

            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer response) throws Exception {

                Util.printLine("map 线程:" + Thread.currentThread().getName() + "\n");
                return response + "：：";
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Util.printLine("doOnNext 线程:" + Thread.currentThread().getName() + "\n");

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String data) throws Exception {
                        Util.printLine("subscribe 线程:" + Thread.currentThread().getName() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Util.printLine("subscribe 线程:" + Thread.currentThread().getName() + "\n");

                        Util.printLine("失败：" + throwable.getMessage() + "\n");
                    }
                });
    }
}
