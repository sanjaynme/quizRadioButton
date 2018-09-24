package com.curiousca.squiz.retrofit;

import com.curiousca.squiz.model.ApiObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public class ApiService {

    private static final String BASE_URL = "http://192.168.1.144/";

    public static RetrofitInterface getServiceClass() {
        return RetrofitAPI.getRetrofit(BASE_URL)
                .create(RetrofitInterface.class);
    }

    public interface RetrofitInterface {
        @GET("sync/quiz.php")
        public Call<List<ApiObject>> getAllPost();

        @GET("sync/gk.php")
        public Call<List<ApiObject>> getGkPost();

        @GET("sync/science.php")
        public Call<List<ApiObject>> getSciencePost();

    }
}
