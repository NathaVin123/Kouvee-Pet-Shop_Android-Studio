package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiLayanan {
    @GET("layanan")
    Call<Response> getAll();

    @GET("layanan/softDelete")
    Call<Response> getSoftDelete();

    @GET("layanan/{cari}")
    Call<Response> getLayanan(@Path("cari") String cari);

    @POST("layanan")
    @FormUrlEncoded
    Call<Response> createLayanan(@Field("nama_layanan") String nama_layanan,
                                 @Field("harga_layanan") Double harga_layanan,
                                 @Field("id_ukuran") int id_ukuran,
                                 @Field("updateLogId") String NIP);

    @POST("layanan/update/{id}")
    @FormUrlEncoded
    Call<Response> updateLayanan(@Path("id") String id_layanan,
                               @Field("nama_layanan") String nama_layanan,
                               @Field("harga_layanan") Double harga_layanan,
                               @Field("id_ukuran") int id_ukuran,
                               @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("layanan/{id}")
    Call<Response> deleteLayanan(@Path("id") String id_layanan,
                                 @Field("updateLogId") String NIP);
    @FormUrlEncoded
    @POST("layanan/{id}/restore")
    Call<Response> restoreLayanan(@Path("id") String id_layanan,
                                  @Field("updateLogId") String NIP);

    @DELETE("layanan/{id}/permanen")
    Call<Response> deleteLayananPermanen(@Path("id") String id_layanan);
}
