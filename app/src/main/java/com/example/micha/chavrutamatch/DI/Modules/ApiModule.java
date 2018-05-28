package com.example.micha.chavrutamatch.DI.Modules;

import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.Data.Http.MyChavrutaAPI;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    public final String BASE_URL = "http://brightlightproductions.online/";

    @Provides
    @MAScope
    public Retrofit provideRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @MAScope
    public MyChavrutaAPI provideApiService(){
        return provideRetrofit(BASE_URL).create(MyChavrutaAPI.class);
    }
}
