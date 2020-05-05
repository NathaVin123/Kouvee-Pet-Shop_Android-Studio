package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiPengadaan {

    @GET("pengadaan")
    Call<Response> getAll();

    @GET("pengadaan/nomorPO")
    Call<String> getNomorPO();

    @POST("pengadaan")
    @FormUrlEncoded
    Call<Response> createPengadaan(@Field("id_supplier") int id_supplier);

    @POST("pengadaan/{id}")
    @FormUrlEncoded
    Call<Response> updatePengadaan(@Path("id") String nomorPO,
                                    @Field("id_supplier") int id_supplier,
                                    @Field("totalHarga_po") Double totalHarga_po,
                                    @Field("status_po") String status_po);

    @POST("pengadaan/updateStatusPengadaan/{id}")
    @FormUrlEncoded
    Call<Response> updateStatusPengadaan(@Path("id") String nomorPO,
                                         @Field("status_po") String status_po);

    @POST("pengadaan/updateTotalHarga/{id}")
    @FormUrlEncoded
    Call<Response> updateBiayaPengadaan(@Path("id") String nomorPO,
                                        @Field("totalHarga_po") Double totalHarga_po);

    @DELETE("pengadaan/{id}")
    Call<Response> batalPengadaan(@Path("id") String nomorPO);
}
