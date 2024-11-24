package com.eldroidfri730.extr.data;

import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.data.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("register")
    Call<mUser> registerUser(@Body mUser user);

    @POST("login")
    Call<LoginResponse> loginUser(@Body mUser user);

    @POST("password/forgot")
    Call<mUser> forgotPassword(@Body mUser user);

    @POST("addCategory")
    Call<mCategory> addCategory(@Body mCategory category);

    @POST("userCategories")
    Call<List<mCategory>> getCategoriesByUserId(@Query("userId") String userId);

}
