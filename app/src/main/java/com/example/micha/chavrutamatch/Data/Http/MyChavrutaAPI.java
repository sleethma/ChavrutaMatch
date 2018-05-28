package com.example.micha.chavrutamatch.Data.Http;

import com.example.micha.chavrutamatch.Data.Http.APIModels.MyChavrutas;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyChavrutaAPI {
    @GET("secure_get_json_my_chavrutas.php/")
    Call<MyChavrutas> getMyChavrutas(@Query("user_id") String userId);

    @GET("secure_get_json_my_chavrutas.php/")
    Observable<MyChavrutas> getMyChavrutasObservable(@Query("user_id") String userId);
}
