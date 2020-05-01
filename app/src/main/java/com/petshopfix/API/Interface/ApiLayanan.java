package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("layanan/update/{id}")
    Call<Response> updateLayanan(@Path("id") String id_layanan,
                               @Part("nama_layanan") RequestBody nama_layanan,
                               @Part("harga_layanan") RequestBody harga_layanan,
                               @Part("id_ukuran") RequestBody id_ukuran,
                               @Part("updateLogId") RequestBody NIP);

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
