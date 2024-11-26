package com.eldroidfri730.extr.data;

import com.eldroidfri730.extr.data.models.mBudget;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.data.response.LoginResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("register")
    Call<mUser> registerUser(@Body mUser user);

    @POST("login")
    Call<LoginResponse> loginUser(@Body mUser user);

    @POST("password/forgot")
    Call<mUser> forgotPassword(@Body mUser user);
    @GET("users")
    Call<List<mUser>> getUsers();

    @Multipart
    @POST("updateUser/{userId}?_method=PATCH")
    Call<mUser> updateUser(
            @Path("userId") String userId,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part profileImage,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody password_confirmation
    );

    @POST("addCategory")
    Call<mCategory> addCategory(@Body mCategory category);

    @POST("userCategories")
    Call<List<mCategory>> getCategoriesByUserId(@Query("userId") String userId);

    @FormUrlEncoded
    @PATCH("updateCategory/{categoryTitle}")
    Call<mCategory> patchCategory(
            @Path("categoryTitle") String categoryTitle,
            @Field("userId") String userId,
            @Field("categoryTitle") String newCategoryTitle

    );

    @DELETE("deleteCategory/{categoryTitle}")
    Call<mCategory> deleteCategory(
            @Path("categoryTitle") String categoryTitle,
            @Query("userId") String userId
    );

    @POST("addBudget")
    Call<mBudget> addBudget(@Body mBudget expense);

    @POST("userBudgets")
    Call<List<mBudget>> getBudgetByUserId(@Query("userId") String userId);

    @PATCH("updateBudget/{categoryTitle}")
    Call<mBudget> patchBudget(
            @Path("categoryTitle") String categoryTitle,
            @Body mBudget budget
    );


    @POST("addExpense")
    Call<mExpense> addExpense(@Body mExpense expense);

    @POST("userExpenses")
    Call<List<mExpense>> getExpensesByUserId(@Query("userId") String userId);

}
