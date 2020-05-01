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

public interface ApiHewan {

    @GET("hewan")
    Call<Response> getAll();

    @GET("hewan/softDelete")
    Call<Response> getSoftDelete();

    @GET("hewan/{cari}")
    Call<Response> getHewan(@Path("cari") String cari);

    @POST("hewan")
    @FormUrlEncoded
    Call<Response> createHewan(@Field("nama_hewan") String nama_hewan,
                                 @Field("tglLahir_hewan") String tglLahir_hewan,
                                 @Field("id_jenis") int id_jenis,
                                 @Field("id_customer") int id_customer,
                                 @Field("updateLogId") String NIP);

    @Multipart
    @POST("hewan/update/{id}")
    Call<Response> updateHewan(@Path("id") int id_hewan,
                                 @Part("nama_hewan") RequestBody nama_hewan,
                               @Part("tglLahir_hewan") RequestBody tglLahir_hewan,
                                 @Part("id_jenis") RequestBody id_jenis,
                                 @Part("id_customer") RequestBody id_customer,
                                 @Part("updateLogId") RequestBody NIP);

    @FormUrlEncoded
    @POST("hewan/{id}")
    Call<Response> deleteHewan(@Path("id") int id_hewan,
                                 @Field("updateLogId") String NIP);
    @FormUrlEncoded
    @POST("hewan/{id}/restore")
    Call<Response> restoreHewan(@Path("id") int id_hewan,
                                  @Field("updateLogId") String NIP);

    @DELETE("hewan/{id}/permanen")
    Call<Response> deleteHewanPermanen(@Path("id") int id_hewan);
}
