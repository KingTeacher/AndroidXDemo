package com.example.module_rxjava.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Api {
    @GET("index.html")
    Observable<String> login();

}
