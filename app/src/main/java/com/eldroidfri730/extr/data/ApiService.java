package com.eldroidfri730.extr.data;

import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.data.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    Call<mUser> registerUser(@Body mUser user);

    @POST("login")
    Call<LoginResponse> loginUser(@Body mUser user);

    @POST("password/forgot")
    Call<mUser> forgotPassword(@Body mUser user);
}
