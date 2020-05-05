package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import okhttp3.MultipartBody;
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

public interface ApiProduk {
    @GET("produk")
    Call<Response> getAll();

    @GET("produk/sortir/{berdasarkan}")
    Call<Response> sortir(@Path("berdasarkan") String berdasarkan);

    @GET("produk/softDelete")
    Call<Response> getSoftDelete();

    @GET("produk/cari/{cari}")
    Call<Response> getProduk(@Path("cari") String cari);

    @Multipart
    @POST("produk")
    Call<Response> createProduk(@Part("nama_produk") RequestBody nama_produk,
                                @Part("harga_produk") RequestBody harga_produk,
                                @Part("stok") RequestBody stok,
                                @Part("min_stok") RequestBody min_stok,
                                @Part("satuan_produk") RequestBody satuan_produk,
                                @Part MultipartBody.Part gambar,
                                @Part("updateLogId") RequestBody updateLogId);

    @Multipart
    @POST("produk/update/{id}")
    Call<Response> updateProduk (@Path("id") String id_produk,
                                 @Part("nama_produk") RequestBody nama_produk,
                                 @Part("harga_produk") RequestBody  harga_produk,
                                 @Part("stok") RequestBody  stok,
                                 @Part("min_stok") RequestBody min_stok,
                                 @Part("satuan_produk") RequestBody satuan_produk,
                                 @Part MultipartBody.Part gambar,
                                 @Part("updateLogId") RequestBody  updateLogId);

    @FormUrlEncoded
    @POST("produk/{id}")
    Call<Response> deleteProduk(@Path("id") String id_produk,
                                @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("produk/{id}/restore")
    Call<Response> restoreProduk(@Path("id") String id_produk,
                                 @Field("updateLogId") String NIP);

    @DELETE("produk/{id}/permanen")
    Call<Response> deleteProdukPermanen(@Path("id") String id_produk);
}
