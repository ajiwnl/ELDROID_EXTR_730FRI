package com.eldroidfri730.extr.data;

import com.eldroidfri730.extr.data.models.mUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    Call<mUser> registerUser(@Body mUser user);
}
