package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiPegawai {

    @POST("pegawai/login")
    @FormUrlEncoded
    Call<Response> Login(@Field("NIP") String NIP,
                         @Field("password") String password);
}
