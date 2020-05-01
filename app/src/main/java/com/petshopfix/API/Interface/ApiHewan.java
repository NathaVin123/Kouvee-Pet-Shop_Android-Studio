package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

//    @POST("layanan/update/{id}")
//    @FormUrlEncoded
//    Call<Response> updateLayanan(@Path("id") String id_layanan,
//                                 @Field("nama_layanan") String nama_layanan,
//                                 @Field("harga_layanan") Double harga_layanan,
//                                 @Field("id_ukuran") int id_ukuran,
//                                 @Field("updateLogId") String NIP);

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
