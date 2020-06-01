package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiTransaksiProduk {

    @GET("transaksiProduk")
    Call<Response> getAll();

    @GET("transaksiProduk/getNoTransaksi")
    Call<String> getNoTransaksi();

    @GET("transaksiProduk/cari/{cari}")
    Call<Response> getTransaksiProduk(@Path("cari") String no_transaksi);

    @POST("transaksiProduk")
    @FormUrlEncoded
    Call<Response> createPenjualanProduk(@Field("id_customer") String id_customer,
                                         @Field("totalBiaya") Double totalBiaya,
                                         @Field("id_customerService") String id_customerService);

    @POST("transaksiProduk/{id}")
    @FormUrlEncoded
    Call<Response> updatePenjualanProduk(@Path("id") String no_transaksi,
                                         @Field("totalBiaya") Double totalBiaya,
                                         @Field("idCustomer") String id_customer,
                                         @Field("id_customerService") String id_customerService);

    @POST("transaksiProduk/updateTotalBiaya/{id}")
    @FormUrlEncoded
    Call<Response> updateTotalBiayaProduk(@Path("id") String no_transaksi,
                                          @Field("totalBiaya") Double totalBiaya);

    @DELETE("transaksiProduk/{id}")
    Call<Response> batalPenjualanProduk(@Path("id") String no_transaksi);
}
