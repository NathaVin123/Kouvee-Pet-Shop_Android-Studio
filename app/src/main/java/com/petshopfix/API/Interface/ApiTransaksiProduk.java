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

    @POST("transaksiProduk")
    @FormUrlEncoded
    Call<Response> createPenjualanProduk(@Field("id_customerService") String id_customerService);

    @POST("transaksiProduk/{id}")
    @FormUrlEncoded
    Call<Response> updatePenjualanProduk(@Path("id") String nomorPO,
                                         @Field("totalBiaya") Double totalBiaya,
                                         @Field("idCustomer") int id_customer,
                                         @Field("id_customerService") String id_customerService);

    @DELETE("transaksiProduk/{id}")
    Call<Response> batalPenjualanProduk(@Path("id") String no_transaksi);
}
