package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiUkuran {
    @GET("ukuranHewan")
    Call<Response> getAll();

    @GET("ukuranHewan/softDelete")
    Call<Response> getSoftDelete();

    @GET("ukuranHewan/{cari}")
    Call<Response> getUkuran(@Path("cari") String cari);

    @POST("ukuranHewan")
    @FormUrlEncoded
    Call<Response> createUkuran(@Field("nama_ukuran") String nama_ukuran,
                                @Field("updateLogId") String NIP);

    @Multipart
    @POST("ukuranHewan/update/{id}")
    Call<Response> updateUkuran(@Path("id") int id_ukuran,
                                @Part("nama_ukuran") RequestBody nama_ukuran,
                                @Part("updateLogId") RequestBody NIP);
    @FormUrlEncoded
    @POST("ukuranHewan/{id}")
    Call<Response> deleteUkuran(@Path("id") int id_ukuran,
                                @Field("updateLogId") String NIP);
    @FormUrlEncoded
    @POST("ukuranHewan/{id}/restore")
    Call<Response> restoreUkuran(@Path("id") int id_ukuran,
                                 @Field("updateLogId") String NIP);

    @DELETE("ukuranHewan/{id}/permanen")
    Call<Response> deleteUkuranPermanen(@Path("id") int id_ukuran);
}
