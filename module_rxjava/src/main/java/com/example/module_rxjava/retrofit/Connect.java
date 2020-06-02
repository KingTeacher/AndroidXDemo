package com.example.module_rxjava.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Connect {

    public static void connect(final Context context) {
        Retrofit retrofit = RetrofitUtil.create("https://tieba.baidu.com/");
        Api api = retrofit.create(Api.class);
        api.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Connect","onSubscribe");
                    }

                    @Override
                    public void onNext(String loginResponse) {
                        Toast.makeText(context, loginResponse, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
