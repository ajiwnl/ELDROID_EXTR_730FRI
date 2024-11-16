package com.eldroidfri730.extr.data;

import com.eldroidfri730.extr.data.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    Call<User> registerUser(@Body User user);
}
