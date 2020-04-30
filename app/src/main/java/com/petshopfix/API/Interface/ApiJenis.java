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

public interface ApiJenis {

    @GET("jenisHewan")
    Call<Response> getAll();

    @GET("jenisHewan/softDelete")
    Call<Response> getSoftDelete();

    @GET("jenisHewan/{cari}")
    Call<Response> getJenis(@Path("cari") String cari);

    @POST("jenisHewan")
    @FormUrlEncoded
    Call<Response> createJenis(@Field("nama_jenis") String nama_jenis,
                                @Field("updateLogId") String NIP);

    @Multipart
    @POST("jenisHewan/update/{id}")
    Call<Response> updateJenis(@Path("id") int id_jenis,
                                @Part("nama_jenis") RequestBody nama_jenis,
                                @Part("updateLogId") RequestBody NIP);
    @FormUrlEncoded
    @POST("jenisHewan/{id}")
    Call<Response> deleteJenis(@Path("id") int id_jenis,
                                @Field("updateLogId") String NIP);
    @FormUrlEncoded
    @POST("jenisHewan/{id}/restore")
    Call<Response> restoreJenis(@Path("id") int id_jenis,
                                 @Field("updateLogId") String NIP);

    @DELETE("jenisHewan/{id}/permanen")
    Call<Response> deleteJenisPermanen(@Path("id") int id_jenis);
}
